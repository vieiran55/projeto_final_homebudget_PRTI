import Header from "@/_components/Header/Header";
import { Outlet } from "react-router-dom";

export default function PageBase() {
  return (
    <main>
      <div>
        <Header />
      </div>
      <div className="mx-6" style={{ marginTop: "7rem", height: "100vh" }}>
        <Outlet />
      </div>
      {/* <Footer /> */}
    </main>
  );
}
