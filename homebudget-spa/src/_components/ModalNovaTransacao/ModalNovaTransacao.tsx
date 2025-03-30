import axios from "axios";
import { useCallback, useEffect, useState } from "react";
import topbar from "topbar";
import styles from "./ModalNovaTransacao.module.scss";
import CurrencyInput from "react-currency-input-field";
import classNames from "classnames";

interface Props {
  showNovaTransacao: boolean;
  setShowNovaTransacao: React.Dispatch<React.SetStateAction<boolean>>;
  token: string;
}

export default function ModalNovaTransacao({
  setShowNovaTransacao,
  token,
}: Props) {
  const [descricao, setDescricao] = useState("");
  const [valor, setValor] = useState("");
  const [data, setData] = useState("");
  const [categoria, setCategoria] = useState(""); // Categoria ou Fonte
  const [tipoTransacao, setTipoTransacao] = useState("despesa"); // Despesa ou Receita
  const valorNumerico = parseFloat(valor.replace(/\./g, "").replace(",", "."));
  const [fontesReceita, setFotesReceita] = useState<string[]>([]);
  const [categoriasDespesas, setCategoriasDespesas] = useState<string[]>([]);

  const apiUrl = import.meta.env.VITE_BASE_URL;

  const fetchData = useCallback(async () => {
    if (!token) return;
    topbar.show();

    try {
      const [despesasCategorias, receitasFontes] = await Promise.all([
        axios.get(`${apiUrl}/despesas/categorias`, {
          headers: { Authorization: `Bearer ${token}` },
        }),
        axios.get(`${apiUrl}/receitas/fontes`, {
          headers: { Authorization: `Bearer ${token}` },
        }),
      ]);

      const categorias = despesasCategorias.data;
      const fontes = receitasFontes.data;

      setFotesReceita(
        fontes.map((fontes: { descricao: any }) => fontes.descricao).sort(),
      );

      setCategoriasDespesas(
        categorias
          .map((categorias: { descricao: any }) => categorias.descricao)
          .sort(),
      );
    } catch (error) {
      console.error("Erro ao buscar dados financeiros:", error);
    } finally {
      topbar.hide();
    }
  }, []);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const handleSave = async () => {
    if (!descricao || !valor || !data || !categoria) {
      alert("Preencha todos os campos.");
      return;
    }

    const endpoint = tipoTransacao === "despesa" ? "/despesas" : "/receitas";
    const campoCategoria = tipoTransacao === "despesa" ? "categoria" : "fonte"; // Categoria ou Fonte

    try {
      topbar.show();
      await axios.post(
        `${apiUrl}${endpoint}`,
        {
          descricao,
          valor: valorNumerico,
          [campoCategoria]: categoria, // Enviar categoria ou fonte
          data,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      setDescricao("");
      setValor("");
      setData("");
      setCategoria("");
      setShowNovaTransacao(false);
    } catch (error: any) {
      console.error(error);
      alert(
        error.response?.data?.message ||
          "Ocorreu um erro ao cadastrar a transação.",
      );
    } finally {
      topbar.hide();
    }
  };

  return (
    <div className={styles.modal} role="dialog">
      <div className={styles.modal_dialog}>
        <div className={styles.modal_content}>
          <div className={styles.modal_header}>
            <h5 className={styles.modal_title} id="modalLabel">
              {tipoTransacao === "despesa" ? "Nova Despesa" : "Nova Receita"}
            </h5>
            <button
              type="button"
              className={styles.btn_close}
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>

          <div className={styles.modal_body}>
            <form>
              {/* Botão Toggle de Tipo de Transação */}
              <div className="mb-3 text-center">
                <div className="btn-group w-100">
                  <button
                    type="button"
                    className={classNames("btn", {
                      "btn-danger": tipoTransacao === "despesa",
                      "btn-outline-danger": tipoTransacao !== "despesa",
                    })}
                    onClick={() => setTipoTransacao("despesa")}
                  >
                    Despesa
                  </button>
                  <button
                    type="button"
                    className={classNames("btn", {
                      "btn-success": tipoTransacao === "receita",
                      "btn-outline-success": tipoTransacao !== "receita",
                    })}
                    onClick={() => setTipoTransacao("receita")}
                  >
                    Receita
                  </button>
                </div>
              </div>

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

              {/* Categoria ou Fonte */}
              <div className="mb-3">
                <label htmlFor="categoria" className="form-label">
                  {tipoTransacao === "despesa" ? "Categoria" : "Fonte"}
                </label>
                <select
                  id="categoria"
                  className="form-control"
                  value={categoria}
                  onChange={(e) => setCategoria(e.target.value)}
                >
                  <option value="">Selecione</option>
                  {tipoTransacao === "despesa"
                    ? categoriasDespesas.map((categoria) => (
                        <option key={categoria} value={categoria}>
                          {categoria}
                        </option>
                      ))
                    : fontesReceita.map((fonte) => (
                        <option key={fonte} value={fonte}>
                          {fonte}
                        </option>
                      ))}
                </select>
              </div>

              {/* Data */}
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

          {/* Rodapé do Modal */}
          <div className={styles.modal_footer}>
            <button
              type="button"
              className="btn btn-secondary"
              onClick={() => setShowNovaTransacao(false)}
              data-bs-dismiss="modal"
            >
              Cancelar
            </button>
            <button
              type="button"
              className={classNames("btn", {
                "btn-danger": tipoTransacao === "despesa",
                "btn-success": tipoTransacao === "receita",
              })}
              onClick={handleSave}
              data-bs-dismiss="modal"
            >
              Salvar {tipoTransacao === "despesa" ? "Despesa" : "Receita"}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
