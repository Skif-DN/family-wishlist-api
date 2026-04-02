import Button from "./ui/Button";

export default function AddWishModal({
  newWish,
  setNewWish,
  editingWish,
  onSubmit,
  onClose,
}) {
  const MAX_LENGTH = 500;
  const remaining = MAX_LENGTH - newWish.description.length;

  return (
    <div className="wishes-modal-overlay">
      <div className="wishes-modal">
        <p className="wishes-add-wish-title">
          {editingWish ? "Edit Wish" : "Add Wish"}
        </p>

        <input
          placeholder="Title (max 40 chars)"
          maxLength={40}
          value={newWish.title}
          onChange={(e) => setNewWish({ ...newWish, title: e.target.value })}
          required
        />
        <div className="textarea-wrapper">
          <textarea
            maxLength={MAX_LENGTH}
            placeholder="Description"
            value={newWish.description}
            onChange={(e) =>
              setNewWish({ ...newWish, description: e.target.value })
            }
            required
          />
          <span
            className={`char-counter-inside ${remaining === 0 ? "limit" : ""}`}
          >
            {remaining}
          </span>
        </div>

        <input
          type="password"
          placeholder="Owner`s PIN"
          value={newWish.pin}
          onChange={(e) => setNewWish({ ...newWish, pin: e.target.value })}
          required
        />

        <div className="wishes-modal-buttons">
          <Button
            variant="add"
            onClick={onSubmit}
            disabled={
              !newWish.title.trim() ||
              !newWish.description.trim() ||
              !newWish.pin.trim()
            }
          >
            {editingWish ? "Update" : "Add wish"}
          </Button>

          <Button variant="secondary" onClick={onClose}>
            Cancel
          </Button>
        </div>
      </div>
    </div>
  );
}
