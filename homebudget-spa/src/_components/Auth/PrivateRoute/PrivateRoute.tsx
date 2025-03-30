import { useAuth } from "@/_components/Auth/AuthContext/AuthContext";
import Login from "@/Pages/Login/Login";
import { Outlet } from "react-router-dom";
import { ClipLoader } from "react-spinners";
import styles from "./PrivateRoute.module.scss";

export default function PrivateRoute() {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div className={styles.spin_container}>
        <ClipLoader color="#4A90E2" size={50} />
      </div>
    );
  }

  return isAuthenticated ? <Outlet /> : <Login />;
}
