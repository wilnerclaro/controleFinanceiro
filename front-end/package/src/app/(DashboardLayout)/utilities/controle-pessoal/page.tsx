/* eslint-disable react-hooks/rules-of-hooks */
'use client';
import React, { useState, useEffect } from 'react';
import { Grid, Box } from '@mui/material';
import FonteDeRenda, { initialRows as fonteDeRendaInitialRows } from './FonteDeRenda';
import Consolidado, { getTotalPrevisto } from './Consolidado';

export default function controleFinanceiro() {

  const [fontesDeRenda, setFontesDeRenda] = useState([...fonteDeRendaInitialRows]);
  const [consolidados, setConsolidados] = useState([...fonteDeRendaInitialRows]);

  const handleUpdateRows = (newRows: Array<object>) => {
    setConsolidados(newRows);
    getTotalPrevisto(newRows);
  };

  useEffect(() => {
    setConsolidados([...fontesDeRenda]);
  }, [fontesDeRenda]);

  return (
    <>
      <Box>
        <Grid container spacing={3}>
          <Grid item xs={12}>
            <FonteDeRenda row={fontesDeRenda} updateRows={handleUpdateRows}  />
          </Grid>
          <Grid item xs={12}>
            <Consolidado rows={consolidados} updateRows={handleUpdateRows} />
          </Grid>
        </Grid>
      </Box>
    </>
  );
}