import type IDespesas from "@/_interfaces/IDespesas";
import type IReceitas from "@/_interfaces/IReceitas";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import axios from "axios";
import topbar from "topbar";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import IconButton from "@mui/material/IconButton";
import { MdDelete, MdEdit } from "react-icons/md";
import Button from "@mui/material/Button";
import Box from "@mui/material/Box";
import Swal from "sweetalert2";

interface Props {
  dados: IReceitas[] | IDespesas[];
  setRemocao: React.Dispatch<React.SetStateAction<boolean>>;
  remocao: boolean;
  setShowEditarTransacao: React.Dispatch<React.SetStateAction<boolean>>;
  setTransacaoEditada: React.Dispatch<
    React.SetStateAction<IReceitas | IDespesas | null>
  >;
  showEditarTransacao: boolean;
  token: string;
  currentPage: number;
  totalPages: number;
  setCurrentPage: React.Dispatch<React.SetStateAction<number>>;
  setTotalPages: React.Dispatch<React.SetStateAction<number>>;
}

const formatarData = (dataString: string | number | Date) => {
  const data = new Date(dataString + "T00:00:00"); // Garante a data no fuso correto
  return data.toLocaleDateString("pt-BR", { timeZone: "America/Sao_Paulo" });
};

export default function TableTransacoes({
  dados,
  setRemocao,
  remocao,
  setShowEditarTransacao,
  setTransacaoEditada,
  token,
  currentPage,
  setCurrentPage,
  totalPages,
}: Props) {
  const apiUrl = import.meta.env.VITE_BASE_URL;

  const darkTheme = createTheme({
    palette: {
      mode: "dark",
      primary: {
        main: "#90caf9",
      },
      secondary: {
        main: "#f48fb1",
      },
      background: {
        default: "#121212",
        paper: "#1e1e1e",
      },
      text: {
        primary: "#ffffff",
        secondary: "#b3b3b3",
      },
    },
    components: {
      MuiButton: {
        styleOverrides: {
          root: {
            "&.Mui-disabled": {
              color: "#666666",
              backgroundColor: "#333333",
            },
          },
        },
      },
      MuiTableCell: {
        styleOverrides: {
          head: {
            backgroundColor: "#333",
            color: "#fff",
          },
        },
      },
      MuiTableRow: {
        styleOverrides: {
          root: {
            "&:hover": {
              backgroundColor: "#b3b3b3",
              cursor: "pointer",
            },
          },
        },
      },
    },
  });

  const dadosParaEditar = (item: IReceitas | IDespesas) => {
    setTransacaoEditada(item);
    setShowEditarTransacao(true);
  };

  const deletarTransacao = async (id: number, tipo: "receita" | "despesa") => {
    const confirmarExclusao = await Swal.fire({
      title: "Tem certeza?",
      text: "Você não poderá reverter esta ação!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      cancelButtonColor: "#3085d6",
      confirmButtonText: "Sim, excluir!",
      cancelButtonText: "Cancelar",
    });

    if (confirmarExclusao.isConfirmed) {
      try {
        topbar.show();
        const endpoint =
          tipo === "receita" ? `/receitas/${id}` : `/despesas/${id}`;
        await axios.delete(`${apiUrl}${endpoint}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        Swal.fire({
          title: "Transação Deletada com sucesso!",
          icon: "success",
        });
        setRemocao(!remocao);
      } catch (error: any) {
        console.error(error);
        alert(
          error.response?.data?.message ||
            "Ocorreu um erro ao deletar a transação.",
        );
      } finally {
        topbar.hide();
      }
    }
  };
  return (
    <ThemeProvider theme={darkTheme}>
      <TableContainer component={Paper}>
        <Table
          sx={{ minWidth: 500 }}
          size="small"
          aria-label="tabela de transações"
        >
          <TableHead>
            <TableRow>
              <TableCell>Descrição</TableCell>
              <TableCell>Valor</TableCell>
              <TableCell>Data</TableCell>
              <TableCell>Ações</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {dados.length > 0 ? (
              dados.map((item, index) => {
                const tipo = "fonte" in item ? "receita" : "despesa";
                return (
                  <TableRow key={index}>
                    <TableCell>{item.descricao}</TableCell>
                    <TableCell>
                      {Number(item.valor).toLocaleString("pt-BR", {
                        style: "currency",
                        currency: "BRL",
                      })}
                    </TableCell>
                    <TableCell>{formatarData(item.data)}</TableCell>
                    <TableCell padding="none">
                      <IconButton
                        color="primary"
                        onClick={() => dadosParaEditar(item)}
                      >
                        <MdEdit />
                      </IconButton>
                      <IconButton
                        color="error"
                        onClick={() => deletarTransacao(item.id, tipo)}
                      >
                        <MdDelete />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                );
              })
            ) : (
              <TableRow>
                <TableCell colSpan={4} align="center">
                  Nenhuma transação encontrada.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
      {/* Paginação */}
      <Box display="flex" justifyContent="center" mt={3}>
        <Button
          variant="contained"
          color="primary"
          disabled={currentPage === 1}
          onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
        >
          {"<"}
        </Button>
        <Box mx={2} display="flex" alignItems="center">
          Página {currentPage} de {totalPages}
        </Box>
        <Button
          variant="contained"
          color="primary"
          disabled={currentPage === totalPages}
          onClick={() =>
            setCurrentPage((prev) => Math.min(prev + 1, totalPages))
          }
        >
          {">"}
        </Button>
      </Box>
    </ThemeProvider>
  );
}
