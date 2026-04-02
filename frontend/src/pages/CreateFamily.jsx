import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getMyFamily, createFamily } from "../services/api";
import { IoArrowBackSharp } from "react-icons/io5";
import logo from "../images/FamilyWishlistLogo.png";
import Button from "../components/ui/Button";

import "./CreateFamily.css";

export default function CreateFamily() {
  const [familyName, setFamilyName] = useState("");
  const [error, setError] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    const loadFamily = async () => {
      try {
        const family = await getMyFamily();

        if (family) {
          localStorage.setItem("familyId", family.id);
          navigate("/family-members");
        }
      } catch (err) {
        if (err.status !== 409) {
          return;
        }
      }
    };

    loadFamily();
  }, []);

  const handleCreate = async (e) => {
    e.preventDefault();

    try {
      const family = await createFamily(familyName);

      localStorage.setItem("familyId", family.id);
      navigate("/family-members");
      setError("");
      setFamilyName("");
    } catch (error) {
      setError(error.message);
    }
  };

  const handleLogout = () => {
    navigate("/");
  };

  return (
    <div className="create-family">
      <Button className="login-back-btn" variant="back" onClick={handleLogout}>
        <IoArrowBackSharp />
      </Button>
      <div>
        <img src={logo} className="create-logo" alt="logo" />
      </div>
      <p className="create-title">Create your family</p>
      <form onSubmit={handleCreate} className="create-form">
        {error && <p style={{ color: "red" }}>{error}</p>}
        <div>
          <input
            maxLength={30}
            placeholder="Family name (max 30 chars)"
            className="create-input"
            value={familyName}
            onChange={(e) => setFamilyName(e.target.value)}
            required
          />
        </div>
        <Button type="submit" variant="secondary" className="create-btn">
          Create
        </Button>
      </form>
    </div>
  );
}
