import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FiEye, FiEyeOff } from "react-icons/fi";
import { getMyFamily, login } from "../services/api";
import { IoArrowBackSharp } from "react-icons/io5";
import logo from "../images/FamilyWishlistLogo.png";
import Button from "../components/ui/Button";
import InfoModal from "../components/ui/InfoModal";
import "./Login.css";

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [message, setMessage] = useState("");

  const navigate = useNavigate();

  const handleInputChange = (setter) => (e) => setter(e.target.value);

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const token = await login(username, password);
      localStorage.setItem("token", token);

      const family = await getMyFamily();

      if (family) {
        localStorage.setItem("familyId", family.familyId);
        navigate("/family-members");
      } else {
        navigate("/create-family");
      }

      setMessage("");
    } catch (error) {
      setMessage(error.message);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("familyId");

    navigate("/");
  };

  return (
    <div className="login">
      <Button className="login-back-btn" variant="back" onClick={handleLogout}>
        <IoArrowBackSharp />
      </Button>
      <div>
        <img src={logo} className="login-logo" alt="logo" />
      </div>
      <p className="login-title">
        Access your Family Wishlist using your account
      </p>
      <form onSubmit={handleLogin} className="login-form">
        <div>
          <input
            value={username}
            className="login-input"
            onChange={handleInputChange(setUsername)}
            placeholder="Username"
            required
          />
        </div>
        <div className="login-password">
          <div className="login-input-wrapper">
            <input
              type={showPassword ? "text" : "password"}
              value={password}
              className="login-input"
              onChange={handleInputChange(setPassword)}
              placeholder="Password"
              required
            />

            <span
              className="login-password-toggle"
              onClick={() => setShowPassword(!showPassword)}
            >
              {showPassword ? <FiEye /> : <FiEyeOff />}
            </span>
          </div>
        </div>
        <Button type="submit" variant="secondary" className="login-btn">
          Login
        </Button>
      </form>
      {message && (
        <InfoModal
          className="login-popup"
          text={message}
          onClose={() => setMessage("")}
        ></InfoModal>
      )}
    </div>
  );
}

export default Login;
