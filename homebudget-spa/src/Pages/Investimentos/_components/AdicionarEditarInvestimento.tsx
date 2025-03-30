import axios from "axios";
import { useCallback, useEffect, useState } from "react";
import topbar from "topbar";
import styles from "../Investimentos.module.scss";
import classNames from "classnames";
import CurrencyInput from "react-currency-input-field";
import type IInvestimentos from "@/_interfaces/IInvestimentos";

interface Props {
  showNovoInvestimento: boolean;
  setShowNovoInvestimento: React.Dispatch<React.SetStateAction<boolean>>;
  token: string;
  investimentoEditando?: IInvestimentos | null;
}

export default function AdicionarEditarInvestimento({
  showNovoInvestimento,
  setShowNovoInvestimento,
  token,
  investimentoEditando,
}: Props) {
  const [descricao, setDescricao] = useState("");
  const [valor, setValor] = useState("");
  const [data, setData] = useState("");
  const [categoria, setCategoria] = useState("");
  const [tiposInvestimentos, setTiposInvestimentos] = useState<string[]>([]);

  const apiUrl = import.meta.env.VITE_BASE_URL;

  useEffect(() => {
    if (investimentoEditando) {
      setDescricao(investimentoEditando.descricao);
      setValor(investimentoEditando.valorAtual.toString());
      setData(investimentoEditando.data);
      setCategoria(investimentoEditando.tipo);
    } else {
      setDescricao("");
      setValor("");
      setData("");
      setCategoria("");
    }
  }, [investimentoEditando]);

  const fetchData = useCallback(async () => {
    if (!token) return;
    topbar.show();

    try {
      const response = await axios.get(`${apiUrl}/investimentos/tipos`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      setTiposInvestimentos(
        response.data
          .map((tipo: { descricao: string }) => tipo.descricao)
          .sort(),
      );
    } catch (error) {
      console.error("Erro ao buscar tipos de investimentos:", error);
    } finally {
      topbar.hide();
    }
  }, [apiUrl, token]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const handleSave = async () => {
    if (!descricao || !valor || !data || !categoria) {
      alert("Preencha todos os campos.");
      return;
    }

    const valorNumerico = parseFloat(
      valor.replace(/\./g, "").replace(",", "."),
    );
    const endpoint = investimentoEditando
      ? `${apiUrl}/investimentos/${investimentoEditando.id}`
      : `${apiUrl}/investimentos`;

    try {
      topbar.show();

      if (investimentoEditando) {
        await axios.put(
          endpoint,
          {
            descricao,
            valorAtual: valorNumerico,
            tipo: categoria,
            data,
          },
          { headers: { Authorization: `Bearer ${token}` } },
        );
      } else {
        await axios.post(
          endpoint,
          {
            descricao,
            valorInicial: valorNumerico,
            valorAtual: valorNumerico,
            tipo: categoria,
            data,
          },
          { headers: { Authorization: `Bearer ${token}` } },
        );
      }

      setShowNovoInvestimento(false);
    } catch (error: any) {
      console.error(error);
      alert(
        error.response?.data?.message ||
          "Ocorreu um erro ao salvar o investimento.",
      );
    } finally {
      topbar.hide();
    }
  };

  return (
    <div
      className={`${styles.modal} ${showNovoInvestimento ? styles.show : ""}`}
      role="dialog"
    >
      <div className={styles.modal_dialog}>
        <div className={styles.modal_content}>
          <div className={styles.modal_header}>
            <h5 className={styles.modal_title}>
              {investimentoEditando
                ? "Editar Investimento"
                : "Novo Investimento"}
            </h5>
            <button
              type="button"
              className={styles.btn_close}
              onClick={() => setShowNovoInvestimento(false)}
            >
              &times;
            </button>
          </div>

          <div className={styles.modal_body}>
            <form>
              <div className="row">
                <div className="col-md-8">
                  <label htmlFor="descricao" className={styles.form_label}>
                    Descrição
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    id="descricao"
                    value={descricao}
                    onChange={(e) => setDescricao(e.target.value)}
                  />
                </div>

                <div className="col-md-4">
                  <label htmlFor="valor" className="form-label">
                    Valor
                  </label>
                  <CurrencyInput
                    id="valor"
                    className="form-control"
                    prefix="R$ "
                    decimalSeparator=","
                    groupSeparator="."
                    value={valor}
                    decimalsLimit={2}
                    onValueChange={(value) => setValor(value ?? "")}
                  />
                </div>
              </div>

              <div className="mb-3">
                <label htmlFor="categoria" className="form-label">
                  Categoria
                </label>
                <select
                  id="categoria"
                  className="form-control"
                  value={categoria}
                  onChange={(e) => setCategoria(e.target.value)}
                >
                  <option value="">Selecione</option>
                  {tiposInvestimentos.map((tipo) => (
                    <option key={tipo} value={tipo}>
                      {tipo}
                    </option>
                  ))}
                </select>
              </div>

              <div className="mb-3">
                <label htmlFor="data" className="form-label">
                  Data
                </label>
                <input
                  type="date"
                  className="form-control"
                  id="data"
                  value={data}
                  onChange={(e) => setData(e.target.value)}
                />
              </div>
            </form>
          </div>

          <div className={styles.modal_footer}>
            <button
              type="button"
              className="btn btn-secondary"
              onClick={() => setShowNovoInvestimento(false)}
            >
              Cancelar
            </button>
            <button
              type="button"
              className={classNames("btn", "btn-primary")}
              onClick={handleSave}
            >
              {investimentoEditando ? "Atualizar" : "Salvar"}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
