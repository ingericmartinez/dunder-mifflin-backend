-- DDL + Data, adapted for H2 MySQL mode
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS purchase_orders;
DROP TABLE IF EXISTS inventory;
DROP TABLE IF EXISTS catalog_products;
DROP TABLE IF EXISTS catalogs;
DROP TABLE IF EXISTS paper_products;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS branches;

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
    status VARCHAR(20) DEFAULT 'created',
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

INSERT INTO branches (branch_id, name, manager) VALUES
('b1', 'Scranton Branch', 'Michael Scott'),
('b2', 'Stamford Branch', 'Josh Porter'),
('b3', 'Albany Branch', 'David Wallace');

INSERT INTO paper_products (product_id, name, description, price_per_box, paper_weight, color) VALUES
('p1', 'Ultra White Bond Paper', 'Premium quality white bond paper for professional documents', 49.99, '20 lb', 'White'),
('p2', 'Recycled Multipurpose Paper', 'Eco-friendly paper made from 100% recycled materials', 42.50, '24 lb', 'Natural'),
('p3', 'Cardstock Premium', 'Heavyweight cardstock for presentations and brochures', 62.75, '65 lb', 'White'),
('p4', 'Pastel Colored Paper', 'Assortment of pastel colored papers for creative projects', 55.25, '32 lb', 'Assorted'),
('p5', 'Legal Parchment Paper', 'Traditional parchment paper for legal documents', 68.00, '28 lb', 'Cream');

INSERT INTO catalogs (catalog_id, name, description) VALUES
('c1', 'Standard Office Catalog', 'Our most popular office papers for everyday use'),
('c2', 'Premium Professional Catalog', 'High-end papers for professional presentations and documents'),
('c3', 'Creative Projects Catalog', 'Specialty papers for creative and artistic projects');

INSERT INTO catalog_products (catalog_id, product_id) VALUES
('c1', 'p1'),
('c1', 'p2'),
('c2', 'p3'),
('c2', 'p5'),
('c3', 'p4');

CREATE INDEX IF NOT EXISTS idx_products_name ON paper_products(name);
CREATE INDEX IF NOT EXISTS idx_catalogs_name ON catalogs(name);
CREATE INDEX IF NOT EXISTS idx_inventory_branch ON inventory(branch_id);
CREATE INDEX IF NOT EXISTS idx_inventory_product ON inventory(product_id);

INSERT INTO customers (customer_id, name, contact_person, email, phone, address) VALUES
('cust1', 'Vance Refrigeration',      'Bob Vance',       'bob.vance@vancefridge.com',    '570-555-0101', '1725 Slough Ave, Scranton, PA 18503'),
('cust2', 'Michael Scott Paper Co.',  'Michael Scott',   'michael@mspc.com',             '570-555-0102', '111 Minersville Hwy, Scranton, PA 18503'),
('cust3', 'Lackawanna County',        'Jim Halpert',     'jim.halpert@lackawanna.gov',   '570-555-0103', '200 Adams Ave, Scranton, PA 18503'),
('cust4', 'Scranton Business Park',   'Todd Packer',     'todd@scrantonbizpark.com',     '570-555-0104', '1725 Slough Ave Suite 200, Scranton, PA 18503'),
('cust5', 'Prince Family Paper',      'Prince Family',   'info@princefamilypaper.com',   '570-555-0105', '340 Penn Ave, Scranton, PA 18503'),
('cust6', 'Sabre Corporation',        'Jo Bennett',      'jo.bennett@sabre.com',         '850-555-0201', '6120 Industrial Blvd, Tallahassee, FL 32301');

INSERT INTO purchase_orders (order_id, customer_id, shipping_address, status, order_date, total_amount) VALUES
('ord1', 'cust1', '1725 Slough Ave, Scranton, PA 18503',           'delivered',   '2025-01-10 09:00:00', 4999.00),
('ord2', 'cust3', '200 Adams Ave, Scranton, PA 18503',             'shipped',     '2025-02-14 10:30:00', 8925.00),
('ord3', 'cust6', '6120 Industrial Blvd, Tallahassee, FL 32301',   'processing',  '2025-03-01 08:15:00', 18837.50),
('ord4', 'cust2', '111 Minersville Hwy, Scranton, PA 18503',       'created',     '2025-03-20 14:00:00', 2125.00),
('ord5', 'cust4', '1725 Slough Ave Suite 200, Scranton, PA 18503', 'delivered',   '2025-01-25 11:00:00', 6275.00),
('ord6', 'cust5', '340 Penn Ave, Scranton, PA 18503',              'canceled',    '2025-02-05 09:45:00', 3400.00);

INSERT INTO order_items (order_id, product_id, quantity, price_per_box, subtotal) VALUES
-- ord1: Vance Refrigeration — 100 cajas Ultra White
('ord1', 'p1', 100, 49.99, 4999.00),
-- ord2: Lackawanna County — mix de productos
('ord2', 'p1',  80, 49.99, 3999.20),
('ord2', 'p2', 115, 42.50, 4887.50),
-- ord3: Sabre Corp — pedido grande
('ord3', 'p3', 100, 62.75, 6275.00),
('ord3', 'p1', 150, 49.99, 7498.50),
('ord3', 'p2', 120, 42.50, 5100.00),
-- ord4: Michael Scott Paper Co. — 50 cajas Recycled
('ord4', 'p2',  50, 42.50, 2125.00),
-- ord5: Scranton Business Park — Cardstock Premium
('ord5', 'p3', 100, 62.75, 6275.00),
-- ord6: Prince Family Paper — Parchment (cancelado)
('ord6', 'p5',  50, 68.00, 3400.00);

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
