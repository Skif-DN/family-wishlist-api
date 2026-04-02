import { useState } from "react";
import Modal from "./Modal";
import Button from "./Button";
import "./ConfirmModal.css";

export default function ConfirmModalPin({ text, onConfirm, onCancel }) {
  const [pin, setPin] = useState("");

  const handleConfirm = () => {
    onConfirm(pin);
  };

  const handleCancel = () => {
    onCancel();
    setPin("");
  };

  return (
    <Modal onClose={handleCancel} closeOnOverlay={false}>
      <div className="comfirm-modal-pin">
        <p className="confirm-modal-text">{text}</p>

        <input
          type="password"
          placeholder="Enter PIN"
          value={pin}
          onChange={(e) => setPin(e.target.value)}
          className="confirm-modal-input"
        />
      </div>
      <div className="confirm-modal-buttons">
        <Button variant="danger" onClick={handleConfirm}>
          OK
        </Button>
        <Button variant="secondary" onClick={handleCancel}>
          Cancel
        </Button>
      </div>
    </Modal>
  );
}
