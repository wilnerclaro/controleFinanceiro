-- Inserção de usuários na tabela tb_usuarios
INSERT INTO tb_usuarios (nome, email, senha, data_criacao, data_atualizacao, ativo) VALUES ('João Silva', 'joao.silva@example.com', 'senhacriptografada', '2023-08-01 10:00:00', '2023-08-01 10:00:00', true)
INSERT INTO tb_usuarios (nome, email, senha, data_criacao, data_atualizacao, ativo) VALUES ('Maria Souza', 'maria.souza@example.com', 'outrasenhacriptografada', '2023-08-01 11:30:00', '2023-08-01 11:30:00', true)
INSERT INTO tb_usuarios (nome, email, senha, data_criacao, data_atualizacao, ativo) VALUES('Pedro Oliveira', 'pedro.oliveira@example.com', 'maisumasenhacriptografada', '2023-08-01 14:15:00', '2023-08-01 14:15:00', true)

-- Inserção de categorias na tabela tb_categorias
INSERT INTO tb_categorias (nome, descricao, tipo, cor, data_criacao, data_atualizacao) VALUES('Alimentação', 'Despesas relacionadas a alimentação', true, 'verde', '2023-08-02 10:00:00', '2023-08-02 10:00:00')
INSERT INTO tb_categorias (nome, descricao, tipo, cor, data_criacao, data_atualizacao) VALUES('Transporte', 'Despesas relacionadas a transporte', true, 'azul', '2023-08-02 11:30:00', '2023-08-02 11:30:00')
INSERT INTO tb_categorias (nome, descricao, tipo, cor, data_criacao, data_atualizacao) VALUES('Lazer', 'Despesas relacionadas a lazer', true, 'amarelo', '2023-08-02 14:15:00', '2023-08-02 14:15:00')
INSERT INTO tb_categorias (nome, descricao, tipo, cor, data_criacao, data_atualizacao) VALUES('Educação', 'Despesas relacionadas a educação', true, 'roxo', '2023-08-02 16:00:00', '2023-08-02 16:00:00')
INSERT INTO tb_categorias (nome, descricao, tipo, cor, data_criacao, data_atualizacao ) VALUES('Saúde', 'Despesas relacionadas a saúde', true, 'vermelho', '2023-08-02 18:30:00', '2023-08-02 18:30:00')

-- Inserção de registros na tabela usuario_categoria
INSERT INTO tb_usuario_categoria (usuario_id, categoria_id) VALUES(1, 1) -- Associação entre usuário 1 e categoria 1
INSERT INTO tb_usuario_categoria (usuario_id, categoria_id) VALUES(1, 2) -- Associação entre usuário 1 e categoria 2
INSERT INTO tb_usuario_categoria (usuario_id, categoria_id) VALUES(2, 3) -- Associação entre usuário 2 e categoria 3
INSERT INTO tb_usuario_categoria (usuario_id, categoria_id) VALUES(2, 4) -- Associação entre usuário 2 e categoria 4
INSERT INTO tb_usuario_categoria (usuario_id, categoria_id) VALUES(3, 1) -- Associação entre usuário 3 e categoria 1
INSERT INTO tb_usuario_categoria (usuario_id, categoria_id) VALUES(3, 4) -- Associação entre usuário 3 e categoria 4
