CREATE DATABASE IF NOT EXISTS Produtos;
USE Produtos;

-- Tabela de produtos
CREATE TABLE IF NOT EXISTS produto (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    descricao TEXT, 
    preco DECIMAL(10,2) NOT NULL,
    image_url VARCHAR(500),
    estoque_quantidade INT NOT NULL
);

-- Tabela de pedidos
CREATE TABLE IF NOT EXISTS pedidos (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    data_pedido DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'PENDENTE',
    cliente_id INT, -- opcional: para futura associação com tabela de cliente
    total DECIMAL(10, 2) DEFAULT 0.00
);

-- Tabela de itens do pedido (associação entre pedido e produto)
CREATE TABLE IF NOT EXISTS itens_pedido (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    produto_id INT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,

    -- Chave estrangeira para pedido
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE,

    -- Chave estrangeira para produto
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);

SHOW CREATE TABLE produto;

USE Produtos;
INSERT INTO produto (nome, descricao, preco, image_url, estoque_quantidade) VALUES
('Base Líquida Matte', 'Base com acabamento matte e longa duração.', 39.90, 'https://placehold.co/150x150/007bff/ffffff?text=Base', 50),
('Batom Vermelho Intenso', 'Batom de alta pigmentação com acabamento cremoso.', 29.90, 'https://placehold.co/150x150/ff0000/ffffff?text=Batom', 80),
-- Adicione os outros 8 itens aqui, sem especificar o 'id'
('Máscara de Cílios Volume', 'Máscara que proporciona volume e definição.', 34.50, 'https://placehold.co/150x150/8a2be2/ffffff?text=Mascara', 100),
('Paleta de Sombras Nude', 'Paleta com tons neutros ideais para o dia a dia.', 59.90, 'https://placehold.co/150x150/deb887/ffffff?text=Paleta', 40),
('Pó Compacto Translúcido', 'Pó fino para selar a maquiagem sem pesar.', 25.00, 'https://placehold.co/150x150/a0a0a0/ffffff?text=Po', 60),
('Blush Rosa Queimado', 'Blush com textura suave e natural.', 32.00, 'https://placehold.co/150x150/ff69b4/ffffff?text=Blush', 70),
('Delineador Líquido Preto', 'Delineador de alta precisão e secagem rápida.', 27.90, 'https://placehold.co/150x150/000000/ffffff?text=Delineador', 90),
('Iluminador Glow Dourado', 'Iluminador com partículas de brilho intenso.', 44.90, 'https://placehold.co/150x150/ffd700/000000?text=Iluminador', 35),
('Lápis de Olho Preto', 'Lápis macio e fácil de esfumar.', 19.90, 'https://placehold.co/150x150/36454F/ffffff?text=Lapis', 85),
('Primer Facial Hidratante', 'Primer que suaviza os poros e hidrata a pele.', 49.90, 'https://placehold.co/150x150/add8e6/000000?text=Primer', 45);


