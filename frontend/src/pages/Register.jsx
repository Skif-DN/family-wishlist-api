import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FiEye, FiEyeOff } from "react-icons/fi";
import { register } from "../services/api";
import { IoArrowBackSharp } from "react-icons/io5";
import logo from "../images/FamilyWishlistLogo.png";
import InfoModal from "../components/ui/InfoModal";
import Button from "../components/ui/Button";
import "./Register.css";

function Register() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [message, setMessage] = useState("");

  const navigate = useNavigate();

  const handleInputChange = (setter) => (e) => setter(e.target.value);

  const handleRegister = async (e) => {
    e.preventDefault();

    try {
      await register(username, password);

      setMessage("User registered successfully!");

      setTimeout(() => {
        navigate("/login");
      }, 2000);
    } catch (error) {
      setMessage(error.message);
    }
  };

  const handleLogout = () => {
    navigate("/");
  };

  return (
    <div className="register">
      <Button
        className="register-back-btn"
        variant="back"
        onClick={handleLogout}
      >
        <IoArrowBackSharp />
      </Button>
      <div>
        <img src={logo} className="register-logo" alt="logo" />
      </div>
      <p className="register-title">Create your Family Wishlist account</p>
      <form onSubmit={handleRegister} className="register-form">
        <div>
          <input
            value={username}
            maxLength={10}
            className="register-input"
            onChange={handleInputChange(setUsername)}
            placeholder="Username"
            required
          />
        </div>
        <div className="register-password-field">
          <div className="register-input-wrapper">
            <input
              type={showPassword ? "text" : "password"}
              value={password}
              className="register-input"
              onChange={handleInputChange(setPassword)}
              placeholder="Password"
              required
            />

            <span
              className="register-password-toggle"
              onClick={() => setShowPassword(!showPassword)}
            >
              {showPassword ? <FiEye /> : <FiEyeOff />}
            </span>
          </div>
          <p className="register-password-title">
            *Username must be 3-10 characters long. Password must be 8-25
            characters long, include upper and lower case letters, a number, and
            a special character
          </p>
        </div>
        <Button type="submit" variant="primary" className="register-btn">
          Register
        </Button>
      </form>
      {message && (
        <InfoModal
          className="register-popup"
          text={message}
          onClose={() => setMessage("")}
        ></InfoModal>
      )}
    </div>
  );
}

export default Register;
