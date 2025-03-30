import avatar from "/imgs/avatar.png";
import styles from "./Avatar.module.scss";
import { useEffect, useState } from "react";

import axios from "axios";
import { useNavigate } from "react-router-dom";

export default function Avatar() {
  const apiUrl = import.meta.env.VITE_BASE_URL;
  const [token, setToken] = useState("");
  const [isOpen, setIsOpen] = useState(false);
  const navigate = useNavigate();

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const [user, setUser] = useState({
    name: "",
    email: "",
  });

  const obterUser = async () => {
    const responseUser = await axios.get(`${apiUrl}/user/me`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    setUser(responseUser.data);
  };

  useEffect(() => {
    const storedToken = localStorage.getItem("tokenNossasFinancas");
    if (storedToken) {
      const parsedToken = JSON.parse(storedToken);
      setToken(parsedToken);
    } else {
      setToken("");
    }
    console.log(token);
  }, []);

  useEffect(() => {
    if (token) {
      obterUser();
    }
  }, [token]);

  const onLogout = () => {
    localStorage.removeItem("tokenNossasFinancas");
    setToken("");
    navigate("/login");
  };

  return (
    <div className={styles.avatarContainer}>
      <div className={styles.avatar} onClick={toggleDropdown}>
        <img src={avatar} alt="Avatar" />
        <h2>Olá, {user.name}</h2>
        <i className={`fas fa-caret-down ${isOpen ? styles.rotate : ""}`}></i>
      </div>
      {isOpen && (
        <div className={styles.dropdownMenu}>
          <button onClick={onLogout} className={styles.dropdownItem}>
            Logout
          </button>
        </div>
      )}
    </div>
  );
}
