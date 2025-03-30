import type IInvestimentos from "@/_interfaces/IInvestimentos";
import DadosCarteira from "@/Pages/GerenciamentoTransacoes/_components/DadosCarteira/DadosCarteira";
import WalletToggle from "@/Pages/GerenciamentoTransacoes/_components/WalletToggle/WalletToggle";
import TableInvestimentos from "@/Pages/Investimentos/_components/TableInvestimentos";
import { Col, Row } from "@govbr-ds/react-components";
import axios from "axios";
import classNames from "classnames";
import { useCallback, useEffect, useState } from "react";
import DatePicker from "react-datepicker";
import topbar from "topbar";
import styles from "./Investimentos.module.scss";
import AdicionarEditarInvestimento from "@/Pages/Investimentos/_components/AdicionarEditarInvestimento";

export default function Investimentos() {
  const apiUrl = import.meta.env.VITE_BASE_URL;
  const storedToken = localStorage.getItem("tokenNossasFinancas");
  const token = storedToken ? JSON.parse(storedToken) : "";

  const [dados, setDados] = useState<IInvestimentos[]>([]);

  const [totals, setTotals] = useState({
    mes: 0,
    consolidado: 0,
  });

  const [remocao, setRemocao] = useState(false);
  const [openWallet, setOpenWallet] = useState(true);
  const [showNovoInvestimento, setShowNovoInvestimento] = useState(false);
  //@ts-ignore
  const [showEditarTransacao, setShowEditarTransacao] = useState(false);
  //@ts-ignore
  const [transacaoEditada, setTransacaoEditada] =
    useState<IInvestimentos | null>(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);

  const [investimentoEditando, setInvestimentoEditando] =
    useState<IInvestimentos | null>(null);

  const agora = new Date();
  const [mes, setMes] = useState(agora.getMonth() + 1);
  const [ano, setAno] = useState(agora.getFullYear());

  const fetchData = useCallback(async () => {
    if (!token) return;
    topbar.show();

    try {
      const [
        investimentosMensais,
        totalInvestimentos,
        investimentosConsolidados,
      ] = await Promise.all([
        axios.get(`${apiUrl}/investimentos-mensais/${mes}/${ano}`, {
          headers: { Authorization: `Bearer ${token}` },
        }),
        axios.get(`${apiUrl}/investimentos/total-mes/${mes}/${ano}`, {
          headers: { Authorization: `Bearer ${token}` },
        }),
        axios.get(
          `${apiUrl}/investimentos/total-mes-consolidadas/${mes}/${ano}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          },
        ),
      ]);

      setDados(investimentosMensais.data.data);

      setTotalPages(investimentosMensais.data.meta.totalPages);

      setTotals({
        mes: totalInvestimentos.data,
        consolidado: investimentosConsolidados.data,
      });
    } catch (error) {
      console.error("Erro ao buscar dados financeiros:", error);
    } finally {
      topbar.hide();
    }
  }, [token, mes, ano, apiUrl]);

  useEffect(() => {
    fetchData();
  }, [fetchData, showNovoInvestimento, showEditarTransacao, remocao, ano, mes]);

  const handleDateChange = (date: Date | null) => {
    if (!date) return;

    const newMonth = date.getMonth() + 1;
    const newYear = date.getFullYear();

    setMes(newMonth);
    setAno(newYear);
  };
  return (
    <>
      <section className={styles.m4}>
        {showNovoInvestimento && (
          <AdicionarEditarInvestimento
            showNovoInvestimento={showNovoInvestimento}
            setShowNovoInvestimento={setShowNovoInvestimento}
            token={token}
            investimentoEditando={investimentoEditando}
          />
        )}
        <>
          <div className={classNames("text-center p-0 rounded")}>
            <Row>
              <Col>
                <div className={styles.botoes_secao}>
                  <button
                    className="btn btn-primary align-self-start"
                    onClick={() => setShowNovoInvestimento(true)}
                  >
                    <i className="fas fa-plus"></i>
                    Nova Transação
                  </button>
                </div>
              </Col>
              <Col>
                <div className={styles.datepicker_container}>
                  <DatePicker
                    selected={new Date(ano, mes - 1)}
                    onChange={handleDateChange}
                    dateFormat="MM/yyyy"
                    showMonthYearPicker
                    className={classNames(
                      "form-control text-center h3 m-0 ",
                      styles.datepicker_shadow,
                    )}
                    customInput={
                      <input
                        type="text"
                        className="form-control text-center"
                        value={`${String(mes).padStart(2, "0")}/${ano}`}
                        readOnly
                      />
                    }
                  />
                </div>
              </Col>
            </Row>
            <div
              className=" d-flex flex-row justify-content-between align-items-start mt-6"
              style={{ height: "100%" }}
            >
              <Col md={12} className="p-2">
                <h3 className="text-center" style={{ color: "#333" }}>
                  Investimentos
                </h3>
                <TableInvestimentos
                  dados={dados}
                  setRemocao={setRemocao}
                  remocao={remocao}
                  setShowEditarTransacao={setShowNovoInvestimento}
                  setTransacaoEditada={setTransacaoEditada}
                  showEditarTransacao={showEditarTransacao}
                  token={token}
                  currentPage={currentPage}
                  setInvestimentoEditando={setInvestimentoEditando}
                  totalPages={totalPages}
                  setCurrentPage={setCurrentPage}
                  setTotalPages={setTotalPages}
                />
              </Col>
            </div>
          </div>
        </>
        <div className="d-flex flex-row justify-content-between align-items-center">
          <div className={styles.carteira_container}>
            <div
              className={classNames(styles.wallet_container)}
              onClick={() => setOpenWallet(!openWallet)}
              title={!openWallet ? "Abrir Carteira" : "Fechar Carteira"}
            >
              <WalletToggle
                openWallet={openWallet}
              />
            </div>
            {openWallet && (
              <div className={styles.carteira}>
                <div className="d-flex flex-row">
                  <DadosCarteira
                    valor={totals.mes}
                    titulo="Investimento no Período"
                    classe="total"
                  />
                  <DadosCarteira
                    valor={totals.consolidado}
                    titulo="Total Investido"
                    classe="total"
                  />
                </div>
              </div>
            )}
          </div>
        </div>
      </section>
    </>
  );
}
