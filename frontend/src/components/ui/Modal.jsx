import "./Modal.css";

export default function Modal({
  children,
  onClose,
  closeOnOverlay = true,
  className = "",
}) {
  const handleOverlayClick = () => {
    if (closeOnOverlay) {
      onClose();
    }
  };

  return (
    <div className={"modal-overlay"} onClick={handleOverlayClick}>
      <div className={"modal-window"} onClick={(e) => e.stopPropagation()}>
        {children}
      </div>
    </div>
  );
}
