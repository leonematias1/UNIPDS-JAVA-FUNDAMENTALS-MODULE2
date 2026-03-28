use cardapio;
-- select * from item_cardapio
create table item_cardapio(
                              id bigint primary key AUTO_INCREMENT,
                              nome varchar(100) NOT NULL,
                              descricao varchar(1000),
                              categoria ENUM('ENTRADAS','PRATOS_PRINCIPAIS','BEBIDAS','SOBREMESA') NOT NULL,
                              preco DECIMAL(9,2) NOT NULL,
                              preco_promocional DECIMAL(9,2)
);

INSERT INTO item_cardapio (nome, descricao, categoria, preco, preco_promocional)
VALUES
    ('Refresco do Chaves', 'Suco de limão, que parece tamarindo mas tem gosto de groselha.', 'BEBIDAS', 2.99, NULL),

    ('Sanduíche de presunto do Chaves', 'Sanduíche de presunto simples, mas feito com muito amor.', 'PRATOS_PRINCIPAIS', 3.50, 2.99),

    ('Torta da Dona Florinda', 'Torta de frango com recheio cremoso e massa crocante.', 'PRATOS_PRINCIPAIS', 12.99, 10.00),

    ('Pipoca do Quico', 'Balde de pipoca preparado com carinho pelo Quico.', 'PRATOS_PRINCIPAIS', 4.99, 3.99),

    ('Água de jamaica', 'Água aromatizada com hibisco e toque de açúcar.', 'BEBIDAS', 2.50, 2.00),

    ('Café da Dona Florinda', 'Café forte para começar o dia com energia.', 'BEBIDAS', 1.99, 1.50),

    ('Churros do Chaves', 'Churros recheados com doce de leite, clássico e irresistível.', 'SOBREMESA', 4.99, 3.99),

    ('Gelatina do Nhonho', 'Gelatina de várias cores, a sobremesa favorita do Nhonho.', 'SOBREMESA', 3.50, 2.99),

    ('Bolo de Chocolate da Dona Clotilde', 'Bolo de Chocolate com cobertura de brigadeiro.', 'SOBREMESA', 5.99, 4.99);
