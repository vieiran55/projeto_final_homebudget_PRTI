import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "@/Pages/Home/Home";
import PageBase from "@/Pages/PageBase/PageBase";
import Login from "@/Pages/Login/Login";

import PrivateRoute from "@/_components/Auth/PrivateRoute/PrivateRoute";
import { AuthProvider } from "@/_components/Auth/AuthContext/AuthContext";
import GerenciamentoTransacoes from "@/Pages/GerenciamentoTransacoes/GerenciamentoTransacoes";
import Dashboards from "@/Pages/Dashboards/Dashboards";

export default function AppRouter() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/" element={<PrivateRoute />}>
            <Route path="/" element={<PageBase />}>
              <Route index element={<Home />} />
              <Route
                path="/gerenciamento-transacoes"
                element={<GerenciamentoTransacoes />}
              />
              <Route path="/dashboards" element={<Dashboards />} />
              <Route path="*" element={<h1>404 Not Found</h1>} />
            </Route>
          </Route>
        </Routes>
      </Router>
    </AuthProvider>
  );
}
