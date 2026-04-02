import Button from "./ui/Button";

export default function AddMemberModal({
  newMember,
  setNewMember,
  onSubmit,
  onClose,
}) {
  const handleChange = (e) => {
    const { name, value } = e.target;
    setNewMember((prev) => ({ ...prev, [name]: value }));
  };

  return (
    <div className="family-modal-member-overlay">
      <div className="family-modal-member-form">
        <p className="family-title">Add Family Member</p>

        <form onSubmit={onSubmit}>
          <input
            maxLength={10}
            name="firstName"
            placeholder="First Name (max 10 chars)"
            value={newMember.firstName}
            onChange={handleChange}
            required
          />
          <input
            maxLength={12}
            name="lastName"
            placeholder="Last Name (max 12 chars)"
            value={newMember.lastName}
            onChange={handleChange}
            required
          />

          <input
            type="date"
            name="birthDate"
            value={newMember.birthDate}
            onChange={handleChange}
            required
          />

          <select
            name="gender"
            value={newMember.gender}
            onChange={handleChange}
            required
          >
            <option value="">Select Gender</option>
            <option value="MALE">Male</option>
            <option value="FEMALE">Female</option>
            <option value="OTHER">Other</option>
          </select>

          <input
            type="password"
            name="pin"
            maxLength="4"
            pattern="[0-9]{4}"
            placeholder="PIN code (4 digits)"
            value={newMember.pin}
            onChange={handleChange}
            required
          />

          <div className="family-modal-member-buttons">
            <Button
              type="submit"
              variant="add"
              className="family-modal-member-btn"
            >
              Add Member
            </Button>

            <Button
              type="button"
              variant="secondary"
              className="family-modal-member-btn"
              onClick={onClose}
            >
              Cancel
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
