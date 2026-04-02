import Modal from "./Modal";
import Button from "./Button";
import "./ConfirmModal.css";

export default function ConfirmModal({ text, onConfirm, onCancel }) {
  return (
    <Modal onClose={onCancel} closeOnOverlay={false}>
      <p className="confirm-modal-text">{text}</p>

      <div className="confirm-modal-buttons">
        <Button variant="danger" onClick={onConfirm}>
          OK
        </Button>

        <Button variant="secondary" onClick={onCancel}>
          Cancel
        </Button>
      </div>
    </Modal>
  );
}
