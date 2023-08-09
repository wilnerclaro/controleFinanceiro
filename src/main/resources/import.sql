-- Inserção de usuários na tabela tb_usuarios
INSERT INTO tb_usuarios (nome, email, senha, data_criacao, data_atualizacao, ativo) VALUES ('João Silva', 'joao.silva@example.com', 'senhacriptografada', '2023-08-01 10:00:00', '2023-08-01 10:00:00', true)
INSERT INTO tb_usuarios (nome, email, senha, data_criacao, data_atualizacao, ativo) VALUES ('Maria Souza', 'maria.souza@example.com', 'outrasenhacriptografada', '2023-08-01 11:30:00', '2023-08-01 11:30:00', true)
INSERT INTO tb_usuarios (nome, email, senha, data_criacao, data_atualizacao, ativo) VALUES('Pedro Oliveira', 'pedro.oliveira@example.com', 'maisumasenhacriptografada', '2023-08-01 14:15:00', '2023-08-01 14:15:00', true)

-- Inserção de categorias na tabela tb_categorias
INSERT INTO tb_categorias (nome, descricao, cor, data_criacao, data_atualizacao) VALUES('Alimentação', 'Despesas relacionadas a alimentação',  'verde', '2023-08-02 10:00:00', '2023-08-02 10:00:00')
INSERT INTO tb_categorias (nome, descricao, cor, data_criacao, data_atualizacao) VALUES('Transporte', 'Despesas relacionadas a transporte',  'azul', '2023-08-02 11:30:00', '2023-08-02 11:30:00')
INSERT INTO tb_categorias (nome, descricao, cor, data_criacao, data_atualizacao) VALUES('Lazer', 'Despesas relacionadas a lazer', , 'amarelo', '2023-08-02 14:15:00', '2023-08-02 14:15:00')
INSERT INTO tb_categorias (nome, descricao, cor, data_criacao, data_atualizacao) VALUES('Educação', 'Despesas relacionadas a educação',  'roxo', '2023-08-02 16:00:00', '2023-08-02 16:00:00')
INSERT INTO tb_categorias (nome, descricao, cor, data_criacao, data_atualizacao ) VALUES('Saúde', 'Despesas relacionadas a saúde',  'vermelho', '2023-08-02 18:30:00', '2023-08-02 18:30:00')
INSERT INTO tb_categorias (nome, descricao, cor, data_criacao, data_atualizacao ) VALUES('Fonte Ativa', 'Receita de fonte ativa',  'vermelho', '2023-08-02 18:30:00', '2023-08-02 18:30:00')
INSERT INTO tb_categorias (nome, descricao, cor, data_criacao, data_atualizacao ) VALUES('Fonte Passiva', 'Receita de fonte passiva',  'vermelho', '2023-08-02 18:30:00', '2023-08-02 18:30:00')

-- Inserção de registros na tabela usuario_categoria
INSERT INTO tb_usuario_categoria (usuario_id, categoria_id) VALUES(1, 1) -- Associação entre usuário 1 e categoria 1
INSERT INTO tb_usuario_categoria (usuario_id, categoria_id) VALUES(1, 2) -- Associação entre usuário 1 e categoria 2
INSERT INTO tb_usuario_categoria (usuario_id, categoria_id) VALUES(2, 3) -- Associação entre usuário 2 e categoria 3
INSERT INTO tb_usuario_categoria (usuario_id, categoria_id) VALUES(2, 4) -- Associação entre usuário 2 e categoria 4
INSERT INTO tb_usuario_categoria (usuario_id, categoria_id) VALUES(3, 1) -- Associação entre usuário 3 e categoria 1
INSERT INTO tb_usuario_categoria (usuario_id, categoria_id) VALUES(3, 4) -- Associação entre usuário 3 e categoria 4


INSERT INTO usuario_categoria (usuario_id, categoria_id) VALUES(1, 1)
INSERT INTO usuario_categoria (usuario_id, categoria_id) VALUES(1, 2)
INSERT INTO usuario_categoria (usuario_id, categoria_id) VALUES(2, 3)
INSERT INTO usuario_categoria (usuario_id, categoria_id) VALUES(2, 4)
INSERT INTO usuario_categoria (usuario_id, categoria_id) VALUES(3, 1)

INSERT INTO tb_sub_categoria (nome, descricao, categoria_id,tipo) VALUES('Supermercado', 'Despesas de compras em supermercado', 1,'despesa')
INSERT INTO tb_sub_categoria (nome, descricao, categoria_id,tipo) VALUES('Restaurante', 'Despesas de alimentação fora de casa', 1,'despesa')
INSERT INTO tb_sub_categoria (nome, descricao, categoria_id,tipo) VALUES('Lanches', 'Despesas de lanches rápidos', 1,'despesa')
INSERT INTO tb_sub_categoria (nome, descricao, categoria_id,tipo) VALUES('Transporte Público', 'Despesas de transporte público', 2,'despesa')
INSERT INTO tb_sub_categoria (nome, descricao, categoria_id,tipo) VALUES('Gasolina', 'Despesas de combustível para veículo', 2,'despesa')
INSERT INTO tb_sub_categoria (nome, descricao, categoria_id,tipo) VALUES('Estacionamento', 'Despesas de estacionamento', 2,'despesa')

-- Inserção de subcategorias para a categoria com ID 3
INSERT INTO tb_sub_categoria (nome, descricao, categoria_id,tipo) VALUES('Aluguel', 'Despesas de aluguel', 3,'despesa')
INSERT INTO tb_sub_categoria (nome, descricao, categoria_id,tipo) VALUES('Condomínio', 'Despesas de condomínio', 3,'despesa')
INSERT INTO tb_sub_categoria (nome, descricao, categoria_id,tipo) VALUES('Luz', 'Despesas de energia elétrica', 3,'despesa')

-- Inserção de subcategorias para a categoria com ID 4
INSERT INTO tb_sub_categoria (nome, descricao, categoria_id,tipo) VALUES('Diversão', 'Despesas de lazer e entretenimento', 4,'despesa')
INSERT INTO tb_sub_categoria (nome, descricao, categoria_id,tipo) VALUES('Cinema', 'Despesas de ingressos de cinema', 4,'despesa')
INSERT INTO tb_sub_categoria (nome, descricao, categoria_id,tipo) VALUES('Viagem', 'Despesas de viagens e passeios', 4,'despesa')