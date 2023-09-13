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
export function getTotalRealizado(rows: Array<GridValidRowModel>): number {
  return rows.reduce((acc, row) => acc + (row.realizado || 0), 0);
}

export default function Consolidado(props: ConsolidadoProps) {
  const { rows } = props;
  const [totalPrevisto, setTotalPrevisto] = useState(0);
  const [totalRealizado, setTotalRealizado] = useState(0);

  // Use useEffect para observar as mudanças em "rows"
  useEffect(() => {
    setTotalPrevisto(getTotalPrevisto(rows));
    setTotalRealizado(getTotalRealizado(rows));
  }, [rows]);

  return (
    <div>
      <h2>Resumo Consolidado</h2>
      <p>Total Previsto: {totalPrevisto}</p>
      <p>Total Realizado: {totalRealizado}</p>
      {/* Adicione aqui o código para criar o gráfico */}
    </div>
  );
}