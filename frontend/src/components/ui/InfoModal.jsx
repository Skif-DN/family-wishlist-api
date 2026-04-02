import Modal from "./Modal";
import Button from "./Button";

export default function InfoModal({ text, onClose }) {
  return (
    <Modal onClose={onClose}>
      <p
        style={{
          marginBottom: "1rem",
          textAlign: "center",
          fontSize: "1rem",
        }}
      >
        {text}
      </p>

      <div style={{ textAlign: "center" }}>
        <Button onClick={onClose} variant="primary">
          OK
        </Button>
      </div>
    </Modal>
  );
}
