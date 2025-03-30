import TableTransacoes from "@/_components/TableTransacoes/TableTransacoes";
import type IDespesas from "@/_interfaces/IDespesas";
import type IReceitas from "@/_interfaces/IReceitas";
import { Col, Row } from "@govbr-ds/react-components";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import styles from "../../GerenciamentoTransacoes.module.scss";
import classNames from "classnames";

interface Props {
  dadosReceitas: IReceitas[];
  dadosDespesas: IDespesas[];
  mes: number;
  ano: number;
  currentPage: number;
  totalPages: number;
  setCurrentPage: React.Dispatch<React.SetStateAction<number>>;
  setTotalPages: React.Dispatch<React.SetStateAction<number>>;
  setMes: React.Dispatch<React.SetStateAction<number>>;
  setAno: React.Dispatch<React.SetStateAction<number>>;
  setShowNovaTransacao: React.Dispatch<React.SetStateAction<boolean>>;
  showNovaTransacao: boolean;
  setShowEditarTransacao: React.Dispatch<React.SetStateAction<boolean>>;
  showEditarTransacao: boolean;
  setRemocao: React.Dispatch<React.SetStateAction<boolean>>;
  remocao: boolean;
  setTransacaoEditada: React.Dispatch<
    React.SetStateAction<IReceitas | IDespesas | null>
  >;
  token: string;
  openWallet: boolean;
  setOpenWallet: React.Dispatch<React.SetStateAction<boolean>>;
}

export default function ControleFinanceiro({
  dadosReceitas,
  dadosDespesas,
  mes,
  ano,
  setMes,
  setAno,
  setShowNovaTransacao,
  setRemocao,
  setShowEditarTransacao,
  showEditarTransacao,
  setTransacaoEditada,
  token,
  currentPage,
  setCurrentPage,
  totalPages,
  setTotalPages,
  remocao,
}: Props) {

  const handleDateChange = (date: Date | null) => {
    if (!date) return;

    const newMonth = date.getMonth() + 1;
    const newYear = date.getFullYear();

    setMes(newMonth);
    setAno(newYear);
  };

  return (
    <>
      <div
        className={classNames(
          styles.controle_financeiro,
          "text-center p-0 rounded",
        )}
      >
        <Row>
          <Col>
            <div className={styles.botoes_secao}>
              <button
                className="btn btn-primary align-self-start"
                onClick={() => setShowNovaTransacao(true)}
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
          <Col md={6} className="p-2">
            <h3 className="text-center" style={{ color: "#333" }}>
              Receitas
            </h3>
            <TableTransacoes
              dados={dadosReceitas}
              remocao={remocao}
              setRemocao={setRemocao}
              setShowEditarTransacao={setShowEditarTransacao}
              showEditarTransacao={showEditarTransacao}
              setTransacaoEditada={setTransacaoEditada}
              token={token}
              currentPage={currentPage}
              setCurrentPage={setCurrentPage}
              totalPages={totalPages}
              setTotalPages={setTotalPages}
            />
          </Col>
          <Col md={6} className="p-2">
            <h3 className="text-center" style={{ color: "#333" }}>
              Despesas
            </h3>
            <TableTransacoes
              dados={dadosDespesas}
              setRemocao={setRemocao}
              remocao={remocao}
              setShowEditarTransacao={setShowEditarTransacao}
              showEditarTransacao={showEditarTransacao}
              setTransacaoEditada={setTransacaoEditada}
              token={token}
              currentPage={currentPage}
              setCurrentPage={setCurrentPage}
              totalPages={totalPages}
              setTotalPages={setTotalPages}
            />
          </Col>
        </div>
      </div>
    </>
  );
}
