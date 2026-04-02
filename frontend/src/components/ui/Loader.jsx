import "./Loader.css";

export default function Loader({ text = "Loading...", fullScreen = false }) {
  return (
    <div className={fullScreen ? "loader-overlay" : "loader-container"}>
      <div className="loader-spinner"></div>
      {text && <p className="loader-text">{text}</p>}
    </div>
  );
}
