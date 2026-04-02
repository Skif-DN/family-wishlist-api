import { Link } from "react-router-dom";
import "./Home.css";
import logo from "../images/FamilyWishlistLogo.png";

export default function Home() {
  return (
    <div className="home">
      <div>
        <img src={logo} className="home-logo" alt="logo" />
      </div>

      <p className="home-title">
        Create wishes. Share with your family. Never miss a perfect gift.
      </p>

      <div className="home-buttons">
        <Link to="/register" className="home-btn-primary">
          Register
        </Link>

        <Link to="/login" className="home-btn-secondary">
          Login
        </Link>
      </div>
    </div>
  );
}
