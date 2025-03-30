import styles from "./GerenciamentoTransacoes.module.scss";
import type IDespesas from "@/_interfaces/IDespesas";
import type IReceitas from "@/_interfaces/IReceitas";
import { useState, useEffect, useCallback } from "react";
import axios from "axios";
import classNames from "classnames";
import topbar from "topbar";
import ModalNovaTransacao from "@/_components/ModalNovaTransacao/ModalNovaTransacao";
import ModalEditarTransacao from "@/_components/ModalEditarTransacao/ModalEditarTransacao";
import DadosCarteira from "@/Pages/GerenciamentoTransacoes/_components/DadosCarteira/DadosCarteira";
import ControleFinanceiro from "@/Pages/GerenciamentoTransacoes/_components/ControleFinanceiro/ControleFinanceiro";
import WalletToggle from "@/Pages/GerenciamentoTransacoes/_components/WalletToggle/WalletToggle";
import Investimentos from "@/Pages/Investimentos/Investimentos";

export default function GerenciamentoTransacoes() {
  const apiUrl = import.meta.env.VITE_BASE_URL;
  const storedToken = localStorage.getItem("tokenNossasFinancas");
  const token = storedToken ? JSON.parse(storedToken) : "";

  const [totals, setTotals] = useState({
    receitas: 0,
    despesas: 0,
    investimentos: 0,
    mes: 0,
    consolidado: 0,
  });

  const [dados, setDados] = useState({
    receitas: [] as IReceitas[],
    despesas: [] as IDespesas[],
  });
  const [remocao, setRemocao] = useState(false);
  const [openWallet, setOpenWallet] = useState(true);
  const [isExpanded, setIsExpanded] = useState(false);
  const [showNovaTransacao, setShowNovaTransacao] = useState(false);
  const [showEditarTransacao, setShowEditarTransacao] = useState(false);
  const [transacaoEditada, setTransacaoEditada] = useState<
    IReceitas | IDespesas | null
  >(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);

  const agora = new Date();
  const [mes, setMes] = useState(agora.getMonth() + 1);
  const [ano, setAno] = useState(agora.getFullYear());
  const [activeTab, setActiveTab] = useState(0);

  const toggleExpand = () => setIsExpanded((prev) => !prev);

  const fetchData = useCallback(async () => {
    if (!token) return;
    topbar.show();

    try {
      const [
        despesasMensais,
        receitasMensais,
        totalDespesas,
        totalReceitas,
        totalDespesasConsolidadas,
        totalReceitasConsolidadas,
        totalInvestimentos,
        investimentosConsolidados,
      ] = await Promise.all([
        axios.get(`${apiUrl}/despesas-mensais/${mes}/${ano}`, {
          headers: { Authorization: `Bearer ${token}` },
        }),
        axios.get(`${apiUrl}/receitas-mensais/${mes}/${ano}`, {
          headers: { Authorization: `Bearer ${token}` },
        }),
        axios.get(`${apiUrl}/despesas/total-mes/${mes}/${ano}`, {
          headers: { Authorization: `Bearer ${token}` },
        }),
        axios.get(`${apiUrl}/receitas/total-mes/${mes}/${ano}`, {
          headers: { Authorization: `Bearer ${token}` },
        }),
        axios.get(`${apiUrl}/despesas/total-mes-consolidadas/${mes}/${ano}`, {
          headers: { Authorization: `Bearer ${token}` },
        }),
        axios.get(`${apiUrl}/receitas/total-mes-consolidadas/${mes}/${ano}`, {
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

      setDados({
        despesas: despesasMensais.data.data,
        receitas: receitasMensais.data.data,
      });

      setTotalPages(receitasMensais.data.meta.totalPages);

      setTotals({
        receitas: totalReceitas.data,
        despesas: totalDespesas.data,
        investimentos: totalInvestimentos.data,
        mes: totalReceitas.data - totalDespesas.data - totalInvestimentos.data,
        consolidado:
          totalReceitasConsolidadas.data -
          totalDespesasConsolidadas.data +
          investimentosConsolidados.data,
      });
    } catch (error) {
      console.error("Erro ao buscar dados financeiros:", error);
    } finally {
      topbar.hide();
    }
  }, [token, mes, ano, apiUrl, activeTab]);

  useEffect(() => {
    fetchData();
  }, [fetchData, showNovaTransacao, showEditarTransacao, remocao, ano, mes]);

  const tabContent = [
    {
      title: "Resumo Financeiro",
      content: (
        <>
          <div className={styles.m4}>
            <ControleFinanceiro
              dadosReceitas={dados.receitas}
              dadosDespesas={dados.despesas}
              mes={mes}
              ano={ano}
              setMes={setMes}
              setAno={setAno}
              setShowNovaTransacao={setShowNovaTransacao}
              showNovaTransacao={showNovaTransacao}
              setShowEditarTransacao={setShowEditarTransacao}
              showEditarTransacao={showEditarTransacao}
              setTransacaoEditada={setTransacaoEditada}
              token={token}
              setRemocao={setRemocao}
              remocao={remocao}
              currentPage={currentPage}
              setCurrentPage={setCurrentPage}
              totalPages={totalPages}
              setTotalPages={setTotalPages}
              openWallet={openWallet}
              setOpenWallet={setOpenWallet}
            />
          </div>
          <div className="d-flex flex-row justify-content-between align-items-center">
            <div className={styles.carteira_container}>
              <div
                className={classNames(styles.wallet_container)}
                onClick={() => setOpenWallet(!openWallet)}
                title={!openWallet ? "Abrir Carteira" : "Fechar Carteira"}
              >
                <WalletToggle openWallet={openWallet} />
              </div>
              {openWallet && (
                <div className={styles.carteira}>
                  <div className="d-flex flex-row">
                    <DadosCarteira
                      valor={totals.receitas}
                      titulo="Receitas"
                      classe="receitas"
                    />
                    <DadosCarteira
                      valor={totals.despesas}
                      titulo="Despesas"
                      classe="despesas"
                    />
                  </div>
                  <div
                    className={classNames(
                      "d-flex flex-column-reverse",
                      styles.dropup_container,
                    )}
                  >
                    {[
                      {
                        valor: totals.mes,
                        titulo: "Saldo Disponível",
                        classe: "total",
                      },
                      {
                        valor: totals.consolidado,
                        titulo: "Saldo Total",
                        classe: "total",
                      },
                      {
                        valor: totals.investimentos,
                        titulo: "Aporte em Investimentos",
                        classe: "total",
                        //icon: "fas fa-arrow-right wiggle-icon",
                      },
                    ].map((item, index) => (
                      <div
                        key={index}
                        className={`${styles.dados_carteira_item} ${
                          isExpanded || index === 0
                            ? styles.visible
                            : styles.hidden
                        }`}
                        onClick={index === 0 ? toggleExpand : undefined}
                      >
                        <DadosCarteira {...item} />
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>
          </div>
        </>
      ),
    },
    {
      title: "Investimentos",
      content: (
        <div className={styles.tab_content}>
          <Investimentos />
        </div>
      ),
    },
  ];

  return (
    <section className={styles.gerenciamento_transacoes}>
      {showNovaTransacao && (
        <ModalNovaTransacao
          showNovaTransacao={showNovaTransacao}
          setShowNovaTransacao={setShowNovaTransacao}
          token={token}
        />
      )}
      {showEditarTransacao && (
        <ModalEditarTransacao
          showEditarTransacao={showEditarTransacao}
          setShowEditarTransacao={setShowEditarTransacao}
          setTransacaoEditada={setTransacaoEditada}
          transacaoEditada={transacaoEditada}
          token={token}
        />
      )}
      <div className={styles.tabs_container}>
        <div className={styles.header_container}>
          <h1
            className=" text-center font-bold p-2 m-0"
            style={{ color: "#333" }}
          >
            Gerenciamento de Transações
          </h1>
          <div className="text-center m-4">
            <span className="text-center" style={{ color: "#333" }}>
              Acompanhe suas receitas. despesas e investimentos mensais.
            </span>
          </div>
        </div>
        <div className={styles.tabs}>
          {tabContent.map((tab, index) => (
            <button
              key={index}
              className={`${styles.tab_button} ${index === activeTab ? styles.active : ""}`}
              onClick={() => setActiveTab(index)}
            >
              {tab.title}
            </button>
          ))}
        </div>
        <div className="tab-panel">{tabContent[activeTab].content}</div>
      </div>
    </section>
  );
}
