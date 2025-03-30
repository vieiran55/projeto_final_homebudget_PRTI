import type IInvestimentos from "@/_interfaces/IInvestimentos";
import {
  Box,
  Button,
  createTheme,
  IconButton,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  ThemeProvider,
} from "@mui/material";
import axios from "axios";
import { MdEdit, MdDelete } from "react-icons/md";
import topbar from "topbar";
import Swal from "sweetalert2";

interface Props {
  dados: IInvestimentos[];
  setRemocao: React.Dispatch<React.SetStateAction<boolean>>;
  remocao: boolean;
  setShowEditarTransacao: React.Dispatch<React.SetStateAction<boolean>>;
  setTransacaoEditada: React.Dispatch<
    React.SetStateAction<IInvestimentos | null>
  >;
  showEditarTransacao: boolean;
  token: string;
  currentPage: number;
  totalPages: number;
  setCurrentPage: React.Dispatch<React.SetStateAction<number>>;
  setTotalPages: React.Dispatch<React.SetStateAction<number>>;
  setInvestimentoEditando: React.Dispatch<
    React.SetStateAction<IInvestimentos | null>
  >;
}

const formatarData = (dataString: string | number | Date) => {
  const data = new Date(dataString + "T00:00:00");
  return data.toLocaleDateString("pt-BR", { timeZone: "America/Sao_Paulo" });
};

export default function TableInvestimentos({
  dados,
  setRemocao,
  remocao,
  setShowEditarTransacao,
  token,
  currentPage,
  totalPages,
  setCurrentPage,
  setInvestimentoEditando,
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

  const dadosParaEditar = (item: IInvestimentos) => {
    setInvestimentoEditando(item);
    setShowEditarTransacao(true);
  };

  const deletarTransacao = async (id: number) => {
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
        const endpoint = `/investimentos/${id}`;
        await axios.delete(`${apiUrl}${endpoint}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setRemocao(!remocao);
        Swal.fire({
          title: "Transação Deletada com sucesso!",
          icon: "success",
        });
      } catch (error: any) {
        console.error(error);
        Swal.fire({
          icon: "error",
          title: "Oops...",
          text: "Erro ao excluir transação.",
        });
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
            {
            dados.length > 0 ? ( 
            dados.map((item, index) => {
              return (
                <TableRow key={index}>
                  <TableCell>{item.descricao}</TableCell>
                  <TableCell>
                    {Number(item.valorAtual).toLocaleString("pt-BR", {
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
                      onClick={() => deletarTransacao(item.id)}
                    >
                      <MdDelete />
                    </IconButton>
                  </TableCell>
                </TableRow>
              );
            }))
            : (
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
