import type {
  ReactNode} from "react";
import {
  createContext,
  useContext,
  useState,
  useEffect,
} from "react";
import axios from "axios";
import topbar from "topbar";

const apiUrl = import.meta.env.VITE_BASE_URL;

interface AuthContextType {
  isAuthenticated: boolean;
  login: (email: string, senha: string) => Promise<void>;
  logout: () => void;
  errorMessage: string;
  isLoading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("tokenNossasFinancas");
    if (token) {
      setIsAuthenticated(true);
    }
    setIsLoading(false); // Finaliza o carregamento
  }, []);

  const login = async (email: string, senha: string) => {
    if (!email || !senha) {
      setErrorMessage("Preencha todos os campos.");
      return;
    }
    try {
      topbar.show();
      const response = await axios.post(`${apiUrl}/auth/login`, {
        email,
        password: senha,
      });

      localStorage.setItem(
        "tokenNossasFinancas",
        JSON.stringify(response.data.token),
      );
      setIsAuthenticated(true);
      setErrorMessage(""); // Limpa mensagens de erro
    } catch (error: any) {
      console.error(error);
      setErrorMessage(
        error.response?.data?.message || "Ocorreu um erro ao fazer login.",
      );
    } finally {
      topbar.hide();
    }
  };

  const logout = () => {
    localStorage.removeItem("tokenNossasFinancas");
    setIsAuthenticated(false);
  };

  useEffect(() => {
    const interceptor = axios.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.status === 401) {
          logout();
        }
        return Promise.reject(error);
      },
    );

    return () => {
      axios.interceptors.response.eject(interceptor);
    };
  }, []);

  return (
    <AuthContext.Provider
      value={{ isAuthenticated, login, logout, errorMessage, isLoading }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
