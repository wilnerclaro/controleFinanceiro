/* eslint-disable react/jsx-key */
/* eslint-disable react-hooks/rules-of-hooks */
import * as React from 'react';
import Button from '@mui/material/Button';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Close';
import {
GridRowsProp,
  GridRowModesModel,
  GridRowModes,
  DataGrid,
  GridColDef,
  GridToolbarContainer,
  GridActionsCellItem,
  GridEventListener,
  GridRowId,
  GridRowModel,
  GridRowEditStopReasons,
  GridValidRowModel,
} from '@mui/x-data-grid';
import {
  randomCreatedDate,
  randomPrice,
  randomId,
  randomArrayItem,
} from '@mui/x-data-grid-generator';
import PageContainer from '../../components/container/PageContainer';
import DashboardCard from '../../components/shared/DashboardCard';
import Consolidado, { getTotalPrevisto } from './Consolidado';
import { useState } from 'react';
const roles = ['Market', 'Finance', 'Development'];
const randomRole = () => {
  return randomArrayItem(roles);
};

  interface FonteDeRendaProps {
    row: Array<GridValidRowModel>; // Atualize o tipo para corresponder ao tipo real de seus dados
    updateRows: (newRows: Array<GridValidRowModel>) => void; // Adicione esta propriedade à interface

  }

  export const initialRows: GridRowsProp = [
    {
      id: randomId(),
      fonteAtiva: "Salários",
      previsto: 0,
      realizado: 1,
      joinDate: randomCreatedDate(),
      tipoRenda: "ATIVA",
      role: randomRole(),
    },
    {
      id: randomId(),
      fonteAtiva: "Bolsa de Estudos",
      previsto: 1,
      realizado: 1,
      joinDate: randomCreatedDate(),
      tipoRenda: "ATIVA",
      role: randomRole(),
    },
    {
      id: randomId(),
      fonteAtiva: "13º Salário",
      previsto: 1,
      realizado: randomPrice(),
      joinDate: randomCreatedDate(),
      tipoRenda: "ATIVA",
      role: randomRole(),
    },
    {
      id: randomId(),
      fonteAtiva: "Férias",
      previsto: 1,
      realizado: 1,
      joinDate: randomCreatedDate(),
      tipoRenda: "PASSIVA",
      role: randomRole(),
    },
    {
      id: randomId(),
      fonteAtiva: "Participação de Lucros e Resultados (PLR)",
      previsto: 1,
      realizado: randomPrice(),
      joinDate: randomCreatedDate(),
      tipoRenda: "PASSIVA",
      role: randomRole(),
    },
  ];

  interface EditToolbarProps {
    setRows: (newRows: (oldRows: GridRowsProp) => GridRowsProp) => void;
    setRowModesModel: (
      newModel: (oldModel: GridRowModesModel) => GridRowModesModel,
    ) => void;
  }

  function EditToolbar(props: EditToolbarProps) {
    const { setRows, setRowModesModel } = props;

    const handleClick = () => {
      const id = randomId();
      setRows((oldRows) => [...oldRows, { id, teste: '', age: '', isNew: true }]);
      setRowModesModel((oldModel) => ({
        ...oldModel,
        [id]: { mode: GridRowModes.Edit, fieldToFocus: 'name' },
      }));
    };

    return (
      <GridToolbarContainer>
        <Button color="primary" startIcon={<AddIcon />} onClick={handleClick}>
          Add record
        </Button>
      </GridToolbarContainer>
    );
  }

  export default function fonteDeRenda(props: FonteDeRendaProps) {
    const {row, updateRows } = props;
    const [rows, setRows] = useState(initialRows);
    const [rowModesModel, setRowModesModel] = useState<GridRowModesModel>({});


    const handleRowEditStop: GridEventListener<'rowEditStop'> = (params, event) => {
      if (params.reason === GridRowEditStopReasons.rowFocusOut) {
        event.defaultMuiPrevented = true;
      }
    };

    const handleEditClick = (id: GridRowId) => () => {
      setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.Edit } });
    };

    const handleSaveClick = (id: GridRowId, row: GridValidRowModel = {}) => () => {
        setRowModesModel({
          ...rowModesModel,
          [id]: { mode: GridRowModes.View },
        });
        const linhaEditada = [...props.row];
        const novaLinha = [...rows];
        const rowIndex = linhaEditada.findIndex((row) => row.id === id);
        if (rowIndex === -1) {
        } else {
          const newRow = linhaEditada[rowIndex];
          linhaEditada[rowIndex].previsto = newRow.previsto;
          getTotalPrevisto(linhaEditada);
          props.updateRows(linhaEditada);
          return linhaEditada;
        }
        return linhaEditada;
      };

    const handleDeleteClick = (id: GridRowId) => () => {
      // Filtra as linhas para remover a linha com o ID correspondente
      const updatedRows = rows.filter((row) => row.id !== id);
      // Calcula o total previsto com base nas linhas atualizadas
      getTotalPrevisto(updatedRows);
      // Atualiza as linhas com o total previsto calculado
      setRows(updatedRows);
      // Atualiza as linhas no componente pai usando a função updateRows
      props.updateRows(updatedRows);
    };

    const handleCancelClick = (id: GridRowId) => () => {
      setRowModesModel({
        ...rowModesModel,
        [id]: { mode: GridRowModes.View, ignoreModifications: true },
      });

      const editedRow = rows.find((row) => row.id === id);
      if (editedRow!.isNew) {
        setRows(rows.filter((row) => row.id !== id));
      }
    };

    const processRowUpdate = (newRow: GridRowModel) => {
      const updatedRow = { ...newRow, isNew: false };
      setRows(rows.map((row) => (row.id === newRow.id ? updatedRow : row)));
      const linhaEditada = [...props.row];
      const rowIndex = linhaEditada.findIndex((row) => row.id === newRow.id);
      if (rowIndex === -1) {
        linhaEditada.push(newRow);
        handleSaveClick(newRow.id);
        console.log(newRow.previsto);
        props.updateRows(linhaEditada);
      } else{
        linhaEditada[rowIndex].previsto = newRow.previsto;
        handleSaveClick(linhaEditada[rowIndex].previsto);
        console.log(linhaEditada[rowIndex].previsto);
        props.updateRows(row);
      }
      
      return updatedRow;
    };

    const handleRowModesModelChange = (newRowModesModel: GridRowModesModel) => {
      setRowModesModel(newRowModesModel);
    };

    const columns: GridColDef[] = [ //Para deixar as colunas ajustaveis a tela alterar o elemento width p/ flex:1
      { field: 'fonteAtiva', headerName: 'FONTE DE RENDA',width:250, editable: true },
      {
        field: 'previsto',
        headerName: 'PREVISTO R$',
        type: 'number',
        width:150,
        align: 'left',
        headerAlign: 'left',
        editable: true,
      },
      {
        field: 'realizado',
        headerName: 'REALIZADO R$',
        type: 'number',
        flex:80,
        align: 'left',
        headerAlign: 'left',
        editable: true,
      },
      {
        field: 'joinDate',
        headerName: 'DATA',
        type: 'date',
        flex:80,
        editable: true,
      },
      {
        field: 'tipoRenda',
        headerName: 'TIPO RENDA', // para a fonte  de renda ativa não tera a opção de departamento (categoria)
        flex:80,
        editable: true,
        type: 'singleSelect',
        valueOptions: ['ATIVA', 'PASSIVA'],
      },
      /*{
        field: 'role',
        headerName: 'Department', // para a fonte  de renda ativa não tera a opção de departamento (categoria)
        flex:1,
        editable: true,
        type: 'singleSelect',
        valueOptions: ['Market', 'Finance', 'Development'],
      },*/
      {
        field: 'actions',
        type: 'actions',
        headerName: 'AÇÕES',
        flex:80,
        cellClassName: 'actions',
        getActions: ({ id }) => {
          const isInEditMode = rowModesModel[id]?.mode === GridRowModes.Edit;

          if (isInEditMode) {
            return [
              <GridActionsCellItem
                icon={<SaveIcon />}
                label="Save"
                sx={{
                  color: 'primary.main',
                }}
                onClick={
                  handleSaveClick(id)}
              />,
              <GridActionsCellItem
                icon={<CancelIcon />}
                label="Cancel"
                className="textPrimary"
                onClick={handleCancelClick(id)}
                color="inherit"
              />,
            ];
          }
          return [
            <GridActionsCellItem
              icon={<EditIcon />}
              label="Edit"
              className="textPrimary"
              onClick={handleEditClick(id)}
              color="inherit"
            />,
            <GridActionsCellItem
              icon={<DeleteIcon />}
              label="Delete"
              onClick={handleDeleteClick(id)}
              color="inherit"
            />,
          ];
        },
      },
    ];

    return (
      <>
        <PageContainer
          title="Controle Financeiro"
          description="página para lançar as entradas e saídas"
        >
          <DashboardCard title="Fonte de Renda Ativa/Passiva">
            <div className="flex flex-col h-full w-full">
              <DataGrid
                rows={rows}
                columns={columns}
                editMode="row"
                rowModesModel={rowModesModel}
                onRowModesModelChange={handleRowModesModelChange}
                onRowEditStop={handleRowEditStop}
                processRowUpdate={processRowUpdate}
                slots={{
                  toolbar: EditToolbar,
                }}
                slotProps={{
                  toolbar: { setRows, setRowModesModel },
                }}
              />
            </div>
          </DashboardCard>
        </PageContainer>
      </>
    );
    
  }