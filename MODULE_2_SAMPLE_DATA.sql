-- ========================================
-- Retail Management System - Sample Data
-- Insert realistic convenience store products
-- ========================================

USE retail_management_system;

-- BEVERAGES
INSERT INTO products (product_code, barcode, name, description, brand, category, purchase_price, selling_price, quantity_in_stock, reorder_level, unit, active)
VALUES
('BEV001', '049000050127', 'Coca Cola Classic 12oz', 'Classic Coca Cola soft drink', 'Coca Cola', 'BEVERAGES', 1.50, 2.99, 150, 50, 'CAN', TRUE),
('BEV002', '012000009814', 'Pepsi 12oz', 'Pepsi cola soft drink', 'Pepsi', 'BEVERAGES', 1.50, 2.99, 120, 40, 'CAN', TRUE),
('BEV003', '016500092008', 'Sprite 12oz', 'Lemon-lime flavored soft drink', 'Coca Cola', 'BEVERAGES', 1.50, 2.99, 100, 40, 'CAN', TRUE),
('BEV004', '012195000031', 'Monster Energy 16oz', 'Energy drink', 'Monster', 'BEVERAGES', 2.50, 4.99, 80, 30, 'CAN', TRUE),
('BEV005', '012000111330', 'Red Bull 8.4oz', 'Energy drink with taurine', 'Red Bull', 'BEVERAGES', 2.75, 5.49, 60, 25, 'CAN', TRUE),
('BEV006', '041331119810', 'Orange Juice 64oz', 'Fresh orange juice', 'Tropicana', 'BEVERAGES', 3.50, 5.99, 40, 15, 'BOTTLE', TRUE),
('BEV007', '012345678901', 'Bottled Water 16.9oz', 'Spring water 6-pack', 'Aquafina', 'BEVERAGES', 2.00, 3.99, 200, 80, 'BOTTLE', TRUE),
('BEV008', '089012345678', 'Iced Tea 20oz', 'Ready to drink iced tea', 'Lipton', 'BEVERAGES', 1.75, 3.49, 90, 30, 'BOTTLE', TRUE);

-- SNACKS
INSERT INTO products (product_code, barcode, name, description, brand, category, purchase_price, selling_price, quantity_in_stock, reorder_level, unit, active)
VALUES
('SNK001', '028400011107', 'Lay\'s Original Chips 2.75oz', 'Classic potato chips', 'Lay\'s', 'SNACKS', 0.80, 1.99, 300, 100, 'PACK', TRUE),
('SNK002', '028400012356', 'Doritos Nacho Cheese 2.75oz', 'Flavored tortilla chips', 'Doritos', 'SNACKS', 0.85, 1.99, 250, 80, 'PACK', TRUE),
('SNK003', '078000019400', 'Reese\'s Peanut Butter Cups', 'Peanut butter candy', 'Hershey', 'SNACKS', 0.60, 1.49, 180, 60, 'PACK', TRUE),
('SNK004', '051500070207', 'Snickers Bar 1.86oz', 'Chocolate candy bar', 'Mars', 'SNACKS', 0.55, 1.39, 200, 70, 'PACK', TRUE),
('SNK005', '029200010504', 'Cheetos Puffs 2.75oz', 'Cheese puffs snack', 'Frito-Lay', 'SNACKS', 0.80, 1.99, 150, 50, 'PACK', TRUE);

-- DAIRY
INSERT INTO products (product_code, barcode, name, description, brand, category, purchase_price, selling_price, quantity_in_stock, reorder_level, unit, active)
VALUES
('DAI001', '073513009631', 'Whole Milk 1 Gallon', '1 gallon whole milk', 'Store Brand', 'DAIRY', 2.50, 3.99, 60, 20, 'BOTTLE', TRUE),
('DAI002', '073513009642', '2% Low Fat Milk 1 Gallon', '1 gallon 2% milk', 'Store Brand', 'DAIRY', 2.40, 3.79, 50, 20, 'BOTTLE', TRUE),
('DAI003', '071213004111', 'Butter 1lb', 'Unsalted butter', 'Land O Lakes', 'DAIRY', 3.50, 5.99, 40, 15, 'PACK', TRUE),
('DAI004', '021130000079', 'Yogurt Plain 32oz', 'Plain yogurt', 'Dannon', 'DAIRY', 2.50, 4.49, 35, 12, 'BOTTLE', TRUE),
('DAI005', '041570036509', 'Orange Juice 52oz', 'Premium orange juice', 'Minute Maid', 'DAIRY', 3.00, 5.49, 45, 15, 'BOTTLE', TRUE);

-- GROCERY
INSERT INTO products (product_code, barcode, name, description, brand, category, purchase_price, selling_price, quantity_in_stock, reorder_level, unit, active)
VALUES
('GRO001', '010100002132', 'Bread White 20oz', 'White bread loaf', 'Store Brand', 'GROCERY', 1.50, 2.99, 80, 30, 'PACK', TRUE),
('GRO002', '010200003154', 'Whole Wheat Bread 20oz', 'Whole wheat bread', 'Store Brand', 'GROCERY', 1.75, 3.49, 50, 20, 'PACK', TRUE),
('GRO003', '070320021070', 'Pasta 16oz', 'Spaghetti pasta', 'Barilla', 'GROCERY', 0.90, 1.99, 120, 40, 'PACK', TRUE),
('GRO004', '025000000135', 'Rice 2lb', 'Long grain white rice', 'Uncle Ben\'s', 'GROCERY', 1.50, 3.49, 70, 25, 'PACK', TRUE),
('GRO005', '041318149815', 'Peanut Butter 18oz', 'Creamy peanut butter', 'Jif', 'GROCERY', 2.00, 4.99, 60, 20, 'JAR', TRUE);

-- HOUSEHOLD
INSERT INTO products (product_code, barcode, name, description, brand, category, purchase_price, selling_price, quantity_in_stock, reorder_level, unit, active)
VALUES
('HOU001', '037000022752', 'Paper Towels 4 Pack', 'Double roll paper towels', 'Bounty', 'HOUSEHOLD', 3.50, 5.99, 50, 20, 'PACK', TRUE),
('HOU002', '037000023659', 'Toilet Paper 12 Pack', 'Regular toilet paper', 'Charmin', 'HOUSEHOLD', 5.00, 7.99, 40, 15, 'PACK', TRUE),
('HOU003', '071797007015', 'Dish Soap 24oz', 'Liquid dish soap', 'Dawn', 'HOUSEHOLD', 1.50, 2.99, 100, 30, 'BOTTLE', TRUE),
('HOU004', '052000141308', 'All Purpose Cleaner 32oz', 'Multi-surface cleaner', 'Lysol', 'HOUSEHOLD', 2.50, 4.49, 60, 20, 'BOTTLE', TRUE),
('HOU005', '011111012012', 'Trash Bags 13 Gallon 20ct', 'Kitchen trash bags', 'Hefty', 'HOUSEHOLD', 2.00, 3.99, 80, 30, 'BOX', TRUE);

-- PERSONAL_CARE
INSERT INTO products (product_code, barcode, name, description, brand, category, purchase_price, selling_price, quantity_in_stock, reorder_level, unit, active)
VALUES
('PER001', '041394521509', 'Toothpaste 6oz', 'Mint flavored toothpaste', 'Crest', 'PERSONAL_CARE', 1.50, 2.99, 100, 30, 'TUBE', TRUE),
('PER002', '037000023650', 'Deodorant Stick', 'Antiperspirant deodorant', 'Old Spice', 'PERSONAL_CARE', 2.00, 3.99, 80, 25, 'PIECE', TRUE),
('PER003', '043800097500', 'Shampoo 13.5oz', 'Hair shampoo', 'Pantene', 'PERSONAL_CARE', 2.50, 4.99, 70, 20, 'BOTTLE', TRUE),
('PER004', '078200003043', 'Soap Bar 5oz', 'Bath soap bar 3-pack', 'Dove', 'PERSONAL_CARE', 1.50, 2.99, 120, 40, 'PACK', TRUE),
('PER005', '312546000379', 'Facial Tissues 160ct', 'Kleenex tissues', 'Kleenex', 'PERSONAL_CARE', 1.00, 1.99, 150, 50, 'BOX', TRUE);

-- FROZEN
INSERT INTO products (product_code, barcode, name, description, brand, category, purchase_price, selling_price, quantity_in_stock, reorder_level, unit, active)
VALUES
('FRO001', '043000075289', 'Frozen Pizza 13.3oz', 'Pepperoni frozen pizza', 'DiGiorno', 'FROZEN', 4.00, 7.99, 30, 10, 'PACK', TRUE),
('FRO002', '071921006200', 'Frozen Vegetables 10oz', 'Mixed vegetables', 'Birds Eye', 'FROZEN', 1.75, 2.99, 50, 20, 'PACK', TRUE),
('FRO003', '072100056400', 'Ice Cream 48oz', 'Vanilla ice cream', 'Häagen-Dazs', 'FROZEN', 4.50, 7.99, 25, 10, 'PIECE', TRUE);

-- COFFEE (GROCERY - beverages preparation)
INSERT INTO products (product_code, barcode, name, description, brand, category, purchase_price, selling_price, quantity_in_stock, reorder_level, unit, active)
VALUES
('COF001', '072238041105', 'Ground Coffee 12oz', 'Premium ground coffee', 'Folgers', 'GROCERY', 4.50, 7.99, 45, 15, 'PACK', TRUE);

-- Verify inserted data
SELECT * FROM products;
SELECT COUNT(*) as total_products FROM products;
SELECT category, COUNT(*) as count FROM products GROUP BY category;

