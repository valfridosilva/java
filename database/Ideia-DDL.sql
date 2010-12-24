SET IGNORECASE true;

SET WRITE_DELAY 100 MILLIS;

CREATE TABLE tb_Cliente (
  id_Cliente BIGINT IDENTITY NOT NULL,
  nome VARCHAR_IGNORECASE(100) NOT NULL,
  telefone VARCHAR_IGNORECASE(15) NOT NULL,
  dt_Nascimento DATE,
  endereco VARCHAR_IGNORECASE(100), 
  email VARCHAR_IGNORECASE(50),
  CONSTRAINT pk_cliente primary key (id_Cliente)  
);
alter TABLE TB_Cliente add constraint ix_nome_telefone unique(nome,telefone);

CREATE TABLE tb_categoria(
id_categoria INT NOT NULL IDENTITY,
descricao VARCHAR_IGNORECASE NOT NULL,
CONSTRAINT pk_categoria PRIMARY KEY (id_categoria)
);
ALTER TABLE tb_categoria ADD CONSTRAINT ix_categoria_descricao UNIQUE(descricao);

CREATE TABLE tb_fabricante(
id_fabricante INT NOT NULL IDENTITY,
descricao VARCHAR_IGNORECASE NOT NULL,
CONSTRAINT pk_fabricante PRIMARY KEY (id_fabricante)
);
ALTER TABLE tb_fabricante ADD CONSTRAINT ix_fabricante_descricao UNIQUE(descricao);

CREATE TABLE tb_produto(
id_produto INT NOT NULL IDENTITY,
codigo BIGINT,
descricao VARCHAR_IGNORECASE NOT NULL,
valor DOUBLE,	
id_categoria INT,
id_fabricante INT,
CONSTRAINT pk_produto PRIMARY KEY (id_produto)
);
ALTER TABLE tb_produto ADD FOREIGN KEY (id_categoria) REFERENCES TB_CATEGORIA (id_categoria) ON DELETE NO ACTION;
ALTER TABLE tb_produto ADD FOREIGN KEY (id_fabricante) REFERENCES TB_FABRICANTE (id_fabricante) ON DELETE NO ACTION;

