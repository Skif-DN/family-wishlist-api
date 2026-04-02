import { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import FamilyMemberItem from "../components/FamilyMemberItem";
import ConfirmModal from "../components/ui/ConfirmModal";
import AddMemberModal from "../components/AddMemberModal";
import { getFamilyMembers, addPerson, deletePerson } from "../services/api";
import logo from "../images/FamilyWishlistLogo.png";
import "./FamilyMembers.css";
import Button from "../components/ui/Button";
import Loader from "../components/ui/Loader";

export default function FamilyMembers() {
  const location = useLocation();
  const familyId = location.state?.familyId || localStorage.getItem("familyId");
  const [members, setMembers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [memberToDelete, setMemberToDelete] = useState(null);
  const [showAddModal, setShowAddModal] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false);

  const emptyMember = {
    firstName: "",
    lastName: "",
    birthDate: "",
    gender: "",
    pin: "",
    familyId,
  };

  const [newMember, setNewMember] = useState(emptyMember);
  const navigate = useNavigate();

  useEffect(() => {
    const loadMembers = async () => {
      const start = Date.now();
      setLoading(true);

      try {
        const data = await getFamilyMembers(familyId);
        setMembers(data);
      } catch (err) {
        setError(err.message);
      } finally {
        const elapsed = Date.now() - start;
        const minLoadingTime = 700;

        if (elapsed < minLoadingTime) {
          setTimeout(() => {
            setLoading(false);
          }, minLoadingTime - elapsed);
        } else {
          setLoading(false);
        }
      }
    };

    loadMembers();
  }, [familyId]);

  const handleAddMember = async (e) => {
    e.preventDefault();
    try {
      await addPerson(newMember);
      const updatedMembers = await getFamilyMembers(familyId);
      setMembers(updatedMembers);
      setNewMember({
        firstName: "",
        lastName: "",
        birthDate: "",
        gender: "",
        pin: "",
        familyId,
      });
      setShowAddModal(false);
      setError("");
    } catch (err) {
      setError(err.message);
    }
  };

  const handleDelete = async () => {
    try {
      await deletePerson(memberToDelete.id);

      setMembers((prev) => prev.filter((m) => m.id !== memberToDelete.id));

      setMemberToDelete(null);
    } catch (err) {
      setError(err.message);
    }
  };

  if (loading) {
    return <Loader fullScreen text="Loading family members..." />;
  }

  const handleLogout = () => {
    setShowLogoutModal(true);
  };

  return (
    <div className="family-members">
      <Button
        className="family-logout-btn"
        onClick={handleLogout}
        variant="danger"
      >
        Logout
      </Button>
      {showLogoutModal && (
        <ConfirmModal
          text="Do you want to logout?"
          onConfirm={() => {
            localStorage.removeItem("token");
            localStorage.removeItem("familyId");
            navigate("/");
          }}
          onCancel={() => setShowLogoutModal(false)}
        />
      )}
      <div>
        <img src={logo} className="family-logo" />
      </div>
      <div className="family-members-block">
        <p className="family-title">Family Members</p>

        {members.length === 0 ? (
          <p className="family-members-title">No family members yet</p>
        ) : (
          <ul className="family-members-list">
            {members.map((member) => (
              <FamilyMemberItem
                key={member.id}
                member={member}
                onDelete={setMemberToDelete}
                onOpenWishes={(m) =>
                  navigate("/wishes", { state: { person: m } })
                }
              />
            ))}
          </ul>
        )}
        {memberToDelete && (
          <ConfirmModal
            text={`Delete ${memberToDelete.firstName} ${memberToDelete.lastName}?`}
            onConfirm={handleDelete}
            onCancel={() => setMemberToDelete(null)}
          />
        )}
      </div>

      <Button
        variant="add"
        onClick={() => setShowAddModal(true)}
        className="family-add-btn"
      >
        Add new member
      </Button>

      {showAddModal && (
        <AddMemberModal
          newMember={newMember}
          setNewMember={setNewMember}
          onSubmit={handleAddMember}
          onClose={() => {
            setNewMember(emptyMember);
            setShowAddModal(false);
          }}
        />
      )}
    </div>
  );
}
