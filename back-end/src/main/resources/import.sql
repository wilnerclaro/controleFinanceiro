INSERT INTO USUARIOS  (name, email, status,data_criacao,data_atualizacao)VALUES ('John Doe', 'johndoe@example.com', 'ACTIVE',CURRENT_TIMESTAMP,null)

INSERT INTO USUARIOS  (name, email, status,data_criacao,data_atualizacao)VALUES ('Jane Doe', 'janedoe@example.com', 'ACTIVE',CURRENT_TIMESTAMP,null)

INSERT INTO USUARIOS  (name, email, status,data_criacao,data_atualizacao)VALUES ('Peter Jones', 'peterjones@example.com', 'ACTIVE',CURRENT_TIMESTAMP,null)

INSERT INTO USUARIOS  (name, email, status,data_criacao,data_atualizacao)VALUES ('Maria Silva', 'mariasilva@example.com', 'ACTIVE',CURRENT_TIMESTAMP,null)

INSERT INTO USUARIOS  (name, email, status,data_criacao,data_atualizacao)VALUES ('José Pereira', 'josepereira@example.com', 'ACTIVE',CURRENT_TIMESTAMP,null)

INSERT INTO USUARIOS  (name, email, status,data_criacao,data_atualizacao)VALUES ('Ana Santos', 'anasantos@example.com', 'ACTIVE',CURRENT_TIMESTAMP,null)

INSERT INTO USUARIOS  (name, email, status,data_criacao,data_atualizacao)VALUES ('Luís Silva', 'luissilva@example.com', 'ACTIVE',CURRENT_TIMESTAMP,null)

INSERT INTO USUARIOS  (name, email, status,data_criacao,data_atualizacao)VALUES ('Marta Pereira', 'martapereira@example.com', 'ACTIVE',CURRENT_TIMESTAMP,null)

INSERT INTO USUARIOS  (name, email, status,data_criacao,data_atualizacao)VALUES ('Carlos Santos', 'carlossantos@example.com', 'ACTIVE',CURRENT_TIMESTAMP,null)


INSERT INTO Categorias (nome, descricao, IS_ACTIVE) VALUES ('Salários', 'Receita proveniente de trabalho assalariado', true)
INSERT INTO Categorias (nome, descricao, IS_ACTIVE) VALUES ('Bolsa de Estudos', 'Receita proveniente de bolsas de estudo', true)
INSERT INTO Categorias (nome, descricao, IS_ACTIVE) VALUES ('Investimentos', 'Receita proveniente de investimentos', true)
INSERT INTO Categorias (nome, descricao, IS_ACTIVE) VALUES ('Outras Receitas', 'Receitas diversas não categorizadas', true)

INSERT INTO TRANSACOES (TIPO_TRANSACAO, VALOR_TRANSACAO, categoria_id, DESCRICAO, USER_ID, FORMA_PAGAMENTO) VALUES  ('Despesa', 100.00, 1, 'Compra de material de escritório', 1, 'Débito')
INSERT INTO TRANSACOES (TIPO_TRANSACAO, VALOR_TRANSACAO, categoria_id, DESCRICAO, USER_ID, FORMA_PAGAMENTO) values ('Receita', 2000.00, 2, 'Salário', 1, 'Transferência')
