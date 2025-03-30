import axios from "axios";
import type { SetStateAction} from "react";
import { useState, useEffect, useCallback } from "react";
import topbar from "topbar";
import styles from "./ModalEditarTransacao.module.scss";
import type IDespesas from "@/_interfaces/IDespesas";
import type IReceitas from "@/_interfaces/IReceitas";
import CurrencyInput from "react-currency-input-field";
import classNames from "classnames";

interface Props {
  showEditarTransacao: boolean;
  setShowEditarTransacao: React.Dispatch<React.SetStateAction<boolean>>;
  transacaoEditada: IReceitas | IDespesas | null;
  setTransacaoEditada: React.Dispatch<
    SetStateAction<IReceitas | IDespesas | null>
  >;
  token: string;
}

export default function ModalEditarTransacao({
  setShowEditarTransacao,
  setTransacaoEditada,
  transacaoEditada,
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

  useEffect(() => {
    if (transacaoEditada) {
      setDescricao(transacaoEditada.descricao);
      setValor(String(transacaoEditada.valor));
      setData(transacaoEditada.data);
      setCategoria(
        "fonte" in transacaoEditada
          ? transacaoEditada.fonte
          : transacaoEditada.categoria,
      );
      setTipoTransacao("fonte" in transacaoEditada ? "receita" : "despesa");
    }
  }, [transacaoEditada]);

  const handleSave = async () => {
    if (!descricao || !valor || !data || !categoria) {
      alert("Preencha todos os campos.");
      return;
    }

    const endpoint = tipoTransacao === "despesa" ? "/despesas" : "/receitas";
    const campoCategoria = tipoTransacao === "despesa" ? "categoria" : "fonte";

    try {
      topbar.show();
      await axios.put(
        `${apiUrl}${endpoint}${transacaoEditada ? `/${transacaoEditada.id}` : ""}`,
        {
          descricao,
          valor: valorNumerico,
          [campoCategoria]: categoria,
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
      setShowEditarTransacao(false);
      setTransacaoEditada(null);
    } catch (error: any) {
      console.error(error);
      alert(
        error.response?.data?.message ||
          "Ocorreu um erro ao salvar a transação.",
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
              {tipoTransacao === "despesa"
                ? "Editar Despesa"
                : "Editar Receita"}
            </h5>
            <button
              type="button"
              className={styles.btn_close}
              onClick={() => setShowEditarTransacao(false)}
              aria-label="Close"
            ></button>
          </div>
          <div className={styles.modal_body}>
            <form>
              <div className="mb-3">
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
              <div className="mb-3">
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
                  //@ts-ignore
                  onValueChange={(value, name, values) => {
                    setValor(value ?? "");
                    console.log("Valor numérico:", values?.float); // Depuração
                  }}
                />
              </div>

              {/* Switch para Despesa ou Receita */}
              {/* <div className="mb-3">
                <label className="form-label">Tipo de Transação</label>
                <div className="form-check">
                  <input
                    className="form-check-input"
                    type="radio"
                    name="tipoTransacao"
                    id="despesa"
                    checked={tipoTransacao === "despesa"}
                    onChange={() => setTipoTransacao("despesa")}
                  />
                  <label className="form-check-label" htmlFor="despesa">
                    Despesa
                  </label>
                </div>
                <div className="form-check">
                  <input
                    className="form-check-input"
                    type="radio"
                    name="tipoTransacao"
                    id="receita"
                    checked={tipoTransacao === "receita"}
                    onChange={() => setTipoTransacao("receita")}
                  />
                  <label className="form-check-label" htmlFor="receita">
                    Receita
                  </label>
                </div>
              </div> */}

              {/* Select de Categoria ou Fonte */}
              <div className="mb-3">
                <label
                  htmlFor={tipoTransacao === "despesa" ? "categoria" : "fonte"}
                  className="form-label"
                >
                  {tipoTransacao === "despesa" ? "Categoria" : "Fonte"}
                </label>
                <select
                  id={tipoTransacao === "despesa" ? "categoria" : "fonte"}
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
              className={classNames(styles.btn_cancel, "btn btn-secondary")}
              onClick={() => setShowEditarTransacao(false)}
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
