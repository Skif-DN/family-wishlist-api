import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import Home from "./pages/Home";
import Register from "./pages/Register";
import Login from "./pages/Login";
import CreateFamily from "./pages/CreateFamily";
import FamilyMembers from "./pages/FamilyMembers";
import Wishes from "./pages/Wishes";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/create-family" element={<CreateFamily />} />
        <Route path="/family-members" element={<FamilyMembers />} />
        <Route path="/wishes" element={<Wishes />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
