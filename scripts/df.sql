-- Script de eliminación de tablas (ejecutar primero si es necesario)
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS purchase_orders;
DROP TABLE IF EXISTS inventory;
DROP TABLE IF EXISTS catalog_products;
DROP TABLE IF EXISTS catalogs;
DROP TABLE IF EXISTS paper_products;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS branches;

-- Script de creación de tablas
CREATE TABLE branches (
    branch_id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    manager VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customers (
    customer_id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact_person VARCHAR(100),
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE paper_products (
    product_id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price_per_box DECIMAL(10, 2) NOT NULL,
    paper_weight VARCHAR(50),
    color VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE catalogs (
    catalog_id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE catalog_products (
    catalog_id VARCHAR(36),
    product_id VARCHAR(36),
    added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (catalog_id, product_id),
    FOREIGN KEY (catalog_id) REFERENCES catalogs(catalog_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES paper_products(product_id) ON DELETE CASCADE
);

CREATE TABLE inventory (
    inventory_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id VARCHAR(36) NOT NULL,
    branch_id VARCHAR(36) NOT NULL,
    quantity_in_stock INT NOT NULL DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (product_id, branch_id),
    FOREIGN KEY (product_id) REFERENCES paper_products(product_id) ON DELETE CASCADE,
    FOREIGN KEY (branch_id) REFERENCES branches(branch_id) ON DELETE CASCADE
);

CREATE TABLE purchase_orders (
    order_id VARCHAR(36) PRIMARY KEY,
    customer_id VARCHAR(36) NOT NULL,
    shipping_address TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'created' CHECK (status IN ('created', 'processing', 'shipped', 'delivered', 'canceled')),
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(36) NOT NULL,
    product_id VARCHAR(36) NOT NULL,
    quantity INT NOT NULL,
    price_per_box DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES purchase_orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES paper_products(product_id) ON DELETE CASCADE
);

-- Inserción de datos de catálogo (sucursales)
INSERT INTO branches (branch_id, name, manager) VALUES
('b1', 'Scranton Branch', 'Michael Scott'),
('b2', 'Stamford Branch', 'Josh Porter'),
('b3', 'Albany Branch', 'David Wallace');

-- Inserción de datos de catálogo (productos de papel)
INSERT INTO paper_products (product_id, name, description, price_per_box, paper_weight, color) VALUES
('p1', 'Ultra White Bond Paper', 'Premium quality white bond paper for professional documents', 49.99, '20 lb', 'White'),
('p2', 'Recycled Multipurpose Paper', 'Eco-friendly paper made from 100% recycled materials', 42.50, '24 lb', 'Natural'),
('p3', 'Cardstock Premium', 'Heavyweight cardstock for presentations and brochures', 62.75, '65 lb', 'White'),
('p4', 'Pastel Colored Paper', 'Assortment of pastel colored papers for creative projects', 55.25, '32 lb', 'Assorted'),
('p5', 'Legal Parchment Paper', 'Traditional parchment paper for legal documents', 68.00, '28 lb', 'Cream');

-- Inserción de datos de catálogo (catálogos)
INSERT INTO catalogs (catalog_id, name, description) VALUES
('c1', 'Standard Office Catalog', 'Our most popular office papers for everyday use'),
('c2', 'Premium Professional Catalog', 'High-end papers for professional presentations and documents'),
('c3', 'Creative Projects Catalog', 'Specialty papers for creative and artistic projects');

-- Asociar productos a catálogos
INSERT INTO catalog_products (catalog_id, product_id) VALUES
('c1', 'p1'),
('c1', 'p2'),
('c2', 'p3'),
('c2', 'p5'),
('c3', 'p4');

-- Inserción de datos de inventario inicial
INSERT INTO inventory (product_id, branch_id, quantity_in_stock) VALUES
('p1', 'b1', 75),
('p1', 'b2', 60),
('p1', 'b3', 80),
('p2', 'b1', 120),
('p2', 'b2', 95),
('p2', 'b3', 110),
('p3', 'b1', 40),
('p3', 'b2', 35),
('p3', 'b3', 50),
('p4', 'b1', 30),
('p4', 'b2', 25),
('p4', 'b3', 20),
('p5', 'b1', 15),
('p5', 'b2', 20),
('p5', 'b3', 10);