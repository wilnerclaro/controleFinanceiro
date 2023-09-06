import { GridValidRowModel } from '@mui/x-data-grid';
import React, { useState, useEffect } from 'react';

interface ConsolidadoProps {
  rows: Array<GridValidRowModel>; // Atualize o tipo para corresponder ao tipo real de seus dados
  updateRows: (newRows: Array<GridValidRowModel>) => void; 
  // Adicione esta propriedade à interface
}

export function getTotalPrevisto(rows: Array<GridValidRowModel>): number {
  return rows.reduce((acc, row) => acc + (row.previsto || 0), 0);
}

export default function Consolidado(props: ConsolidadoProps) {
  const { rows } = props;
  const [total, setTotal] = useState(0);

  // Use useEffect para observar as mudanças em "rows"
  useEffect(() => {
    setTotal(getTotalPrevisto(rows));
  }, [rows]);

  return (
    <div>
      <h2>Resumo Consolidado</h2>
      <p>Total Previsto: {total}</p>
      {/* Adicione aqui o código para criar o gráfico */}
    </div>
  );
}