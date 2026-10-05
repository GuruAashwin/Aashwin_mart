-- ===================================================================
-- AASHWIN MART — Master Seed Data
-- ===================================================================

-- Demo Passwords (bcrypt cost 12, individually generated):
-- Admin:  'Admin@123'  -> $2a$12$Yxe0WVTj0baxL9Bah06/Ieq.FKG/aNmdts3LFqMLcZTMWKc5EYhWi
-- Seller: 'Seller@123' -> $2a$12$yKUIGZK4i5lDXtUW74JlCexhaaNz8.Tae7pvkXF5SiAcC99bAXt4O
-- Buyer:  'Buyer@123'  -> $2a$12$2q2gnJtD.r4KCM8jW4B8jepg1TGNiCeFF8kghwW2ApEkQHZLu.cdq

-- 1. SEED USERS
INSERT INTO users (id, name, email, password_hash, role, is_active, created_at) VALUES
(1, 'Aashwin Administrator', 'admin@aashwinmart.com', '$2a$12$Yxe0WVTj0baxL9Bah06/Ieq.FKG/aNmdts3LFqMLcZTMWKc5EYhWi', 'ADMIN', TRUE, CURRENT_TIMESTAMP),
(2, 'Aurum Luxury Living', 'seller.aurum@aashwinmart.com', '$2a$12$yKUIGZK4i5lDXtUW74JlCexhaaNz8.Tae7pvkXF5SiAcC99bAXt4O', 'SELLER', TRUE, CURRENT_TIMESTAMP),
(3, 'Crown & Co. Horology', 'seller.crown@aashwinmart.com', '$2a$12$yKUIGZK4i5lDXtUW74JlCexhaaNz8.Tae7pvkXF5SiAcC99bAXt4O', 'SELLER', TRUE, CURRENT_TIMESTAMP),
(4, 'Regal Tech & Sound', 'seller.regal@aashwinmart.com', '$2a$12$yKUIGZK4i5lDXtUW74JlCexhaaNz8.Tae7pvkXF5SiAcC99bAXt4O', 'SELLER', TRUE, CURRENT_TIMESTAMP),
(5, 'Alex Sterling', 'buyer.alex@aashwinmart.com', '$2a$12$2q2gnJtD.r4KCM8jW4B8jepg1TGNiCeFF8kghwW2ApEkQHZLu.cdq', 'BUYER', TRUE, CURRENT_TIMESTAMP),
(6, 'Sophia Vance', 'buyer.sophia@aashwinmart.com', '$2a$12$2q2gnJtD.r4KCM8jW4B8jepg1TGNiCeFF8kghwW2ApEkQHZLu.cdq', 'BUYER', TRUE, CURRENT_TIMESTAMP),
(7, 'David Windsor', 'buyer.david@aashwinmart.com', '$2a$12$2q2gnJtD.r4KCM8jW4B8jepg1TGNiCeFF8kghwW2ApEkQHZLu.cdq', 'BUYER', TRUE, CURRENT_TIMESTAMP);

-- Reset identity counter for users
ALTER TABLE users ALTER COLUMN id RESTART WITH 8;

-- 2. SEED PRODUCTS (18 Luxury Marketplace Items)
INSERT INTO products (id, seller_id, name, description, price, stock_qty, category, image_url, is_active, created_at) VALUES
(1, 4, 'Sovereign ANC Wireless Gold Headphones', 'Precision acoustic engineering clad in champagne gold brushed aluminum with active noise cancellation and 40-hour lossless playback.', 499.00, 25, 'Electronics', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&auto=format&fit=crop&q=80', TRUE, CURRENT_TIMESTAMP),
(2, 3, 'Crown Royal Chronograph Automatic', 'Handcrafted Swiss automatic movement encased in 18K gold bezel with sapphire crystal and genuine alligator leather strap.', 1850.00, 10, 'Accessories', 'https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=800&auto=format&fit=crop&q=80', TRUE, CURRENT_TIMESTAMP),
(3, 2, '24K Gold Trim Porcelain Espresso Set', 'Artisanal Italian porcelain adorned with real 24-karat gold filigree gilding. Includes 6 cups, saucers, and gold-plated stirring spoons.', 280.00, 30, 'Home', 'https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=800&auto=format&fit=crop&q=80', TRUE, CURRENT_TIMESTAMP),
(4, 2, 'Italian Silk Jacquard Royal Blazer', 'Tailored in Milan from pure mulberry silk jacquard with subtle golden baroque motifs and horn buttons.', 890.00, 15, 'Fashion', 'https://images.unsplash.com/photo-1507679799987-c73779587ccf?w=800&auto=format&fit=crop&q=80', TRUE, CURRENT_TIMESTAMP),
(5, 3, 'Imperial 18K Gold Signet Ring', 'Solid 18K yellow gold signet ring featuring a polished onyx center stone with engraved royal crest.', 650.00, 20, 'Accessories', 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=800&auto=format&fit=crop&q=80', TRUE, CURRENT_TIMESTAMP),
(6, 4, 'Regal Pure Sound Wireless Speaker', 'Luxury acoustic speaker with hand-finished walnut enclosure and brass gold control dials delivering room-filling 360 audio.', 550.00, 18, 'Electronics', 'https://images.unsplash.com/photo-1545454675-3531b543be5d?w=800&auto=format&fit=crop&q=80', TRUE, CURRENT_TIMESTAMP),
(7, 2, 'Royal Damask 1000TC Egyptian Bedding', 'Woven from long-staple Giza Egyptian cotton with subtle gold piping. Exceptionally soft and breathable luxury hotel finish.', 380.00, 40, 'Home', 'https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=800&auto=format&fit=crop&q=80', TRUE, CURRENT_TIMESTAMP),
(8, 3, 'Sovereign Aviator 24K Plated Sunglasses', 'Titanium aviator frame plated in 24K gold with polarized anti-glare lenses and hand-buffed acetate temple tips.', 320.00, 35, 'Accessories', 'https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=800&auto=format&fit=crop&q=80', TRUE, CURRENT_TIMESTAMP),
(9, 4, 'Titanium Pro 4K Cinema Drone', 'Ultra-lightweight aerospace titanium casing with Hasselblad 4K camera and 45-minute obstacle-avoiding smart flight time.', 1450.00, 12, 'Gadgets', 'https://images.unsplash.com/photo-1527977966376-1c8408f9f108?w=800&auto=format&fit=crop&q=80', TRUE, CURRENT_TIMESTAMP),
(10, 2, 'Hand-Blown Crystal Chandelier Lamp', 'Artisan Bohemian crystal teardrops suspended from a polished brass gold geometric frame for ambient warm illumination.', 720.00, 14, 'Home', 'https://images.unsplash.com/photo-1513506003901-1e6a229e2d15?w=800&auto=format&fit=crop&q=80', TRUE, CURRENT_TIMESTAMP),
(11, 2, 'Handcrafted Tuscan Leather Travel Bag', 'Full-grain vegetable-tanned Italian calfskin with solid brass hardware and reinforced canvas lining for discerning travelers.', 780.00, 22, 'Fashion', 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=800&auto=format&fit=crop&q=80', TRUE, CURRENT_TIMESTAMP),
(12, 3, 'Royal Oud Extrait de Parfum (100ml)', 'A regal olfactory masterwork blending rare Cambodian oud, Damascus rose, ambergris, and golden saffron.', 340.00, 50, 'Beauty', 'https://images.unsplash.com/photo-1592945403244-b3fbafd7f539?w=800&auto=format&fit=crop&q=80', TRUE, CURRENT_TIMESTAMP),
(13, 3, '24K Gold Cellular Renewal Serum', 'Infused with colloidal 24K gold flakes, hyaluronic acid, and peptides to promote radiance and cell rejuvenation.', 195.00, 45, 'Beauty', 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=800&auto=format&fit=crop&q=80', TRUE, CURRENT_TIMESTAMP),
(14, 4, 'Imperial Mechanical Keyboard - Gold Edition', 'CNC milled brass body with custom lubricated switches and double-shot PBT keycaps with gold legends.', 360.00, 28, 'Gadgets', 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=800&auto=format&fit=crop&q=80', TRUE, CURRENT_TIMESTAMP),
(15, 4, 'Wireless Magnetic Fast Charging Valet', 'Crafted from black obsidian stone and champagne gold aluminum to wirelessly charge phone, watch, and earbuds simultaneously.', 180.00, 60, 'Gadgets', 'https://images.unsplash.com/photo-1586953208448-b95a79798f07?w=800&auto=format&fit=crop&q=80', TRUE, CURRENT_TIMESTAMP),
(16, 2, 'Heritage Cashmere Overcoat', '100% pure Mongolian grade-A cashmere overcoat in camel gold with silk lining and tailored notch lapels.', 1150.00, 10, 'Fashion', 'https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=800&auto=format&fit=crop&q=80', TRUE, CURRENT_TIMESTAMP),
(17, 3, 'Royal Velvet Evening Loafers', 'Hand-stitched midnight black velvet slippers with gold bullion crest embroidery and quilted satin insole.', 450.00, 16, 'Fashion', 'https://images.unsplash.com/photo-1549298916-b41d501d3772?w=800&auto=format&fit=crop&q=80', TRUE, CURRENT_TIMESTAMP),
(18, 4, 'Sovereign Smart Decanter and Aerator', 'Sommelier-grade digital ultrasonic wine aerator and preservation system with brushed gold accents.', 420.00, 20, 'Gadgets', 'https://images.unsplash.com/photo-1510812431401-41d2bd2722f3?w=800&auto=format&fit=crop&q=80', TRUE, CURRENT_TIMESTAMP);

-- Reset identity counter for products
ALTER TABLE products ALTER COLUMN id RESTART WITH 19;

-- 3. SEED INITIAL COMPLETED ORDERS (For verified reviews and demonstration)
INSERT INTO orders (id, buyer_id, status, total_amount, shipping_address, created_at) VALUES
(1, 5, 'DELIVERED', 499.00, 'Alex Sterling, 742 Evergreen Terrace, Springfield, OR 97477, Phone: +1 555-0199', DATEADD('DAY', -10, CURRENT_TIMESTAMP)),
(2, 6, 'DELIVERED', 1850.00, 'Sophia Vance, 1000 Fifth Avenue, New York, NY 10028, Phone: +1 555-0245', DATEADD('DAY', -7, CURRENT_TIMESTAMP)),
(3, 5, 'SHIPPED', 650.00, 'Alex Sterling, 742 Evergreen Terrace, Springfield, OR 97477, Phone: +1 555-0199', DATEADD('DAY', -2, CURRENT_TIMESTAMP));

ALTER TABLE orders ALTER COLUMN id RESTART WITH 4;

-- 4. SEED ORDER ITEMS
INSERT INTO order_items (id, order_id, product_id, quantity, unit_price) VALUES
(1, 1, 1, 1, 499.00),
(2, 2, 2, 1, 1850.00),
(3, 3, 5, 1, 650.00);

ALTER TABLE order_items ALTER COLUMN id RESTART WITH 4;

-- 5. SEED VERIFIED REVIEWS (Linked to delivered purchases)
INSERT INTO reviews (id, product_id, user_id, rating, comment, created_at) VALUES
(1, 1, 5, 5, 'The soundstage and build quality are simply royal. The champagne gold finish looks mesmerizing on my desk!', DATEADD('DAY', -8, CURRENT_TIMESTAMP)),
(2, 2, 6, 5, 'Unbelievable craftsmanship. The 18K gold bezel and smooth automatic sweep make this timepiece a treasured heirloom.', DATEADD('DAY', -5, CURRENT_TIMESTAMP));

ALTER TABLE reviews ALTER COLUMN id RESTART WITH 3;
