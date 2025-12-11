-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th12 01, 2025 lúc 08:49 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `thietbi`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitietdonhang`
--

CREATE TABLE `chitietdonhang` (
  `id` int(11) NOT NULL,
  `madonhang` int(11) NOT NULL,
  `masanpham` int(11) NOT NULL,
  `tensanpham` varchar(255) NOT NULL,
  `giasanpham` int(11) NOT NULL,
  `soluongsanpham` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `chitietdonhang`
--

INSERT INTO `chitietdonhang` (`id`, `madonhang`, `masanpham`, `tensanpham`, `giasanpham`, `soluongsanpham`) VALUES
(8, 8, 1, 'Giày nam năng động', 4100000, 1),
(9, 9, 3, 'Giày Adidas Chính Hãng - Lightblaze - Màu Đen/Hồng | JapanSport JR1299', 4100000, 1),
(10, 10, 1, 'Giày nam năng động', 4100000, 1),
(11, 11, 5, 'Giày Lacoste Chính hãng - Carnaby Cup Leather Sneakers - Màu Be | JapanSport 7-48SMA0021-4Y0', 4500000, 2),
(12, 11, 6, 'Giày Puma Nam Chính Hãng - Scuderia Ferrari Caven 2.0 - Màu Đen | JapanSport 308901-01', 1000000, 1),
(13, 12, 1, 'Giày nam năng động', 4100000, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `donhang`
--

CREATE TABLE `donhang` (
  `id` int(11) NOT NULL,
  `id_nguoidung` int(11) DEFAULT 0,
  `tenkhachhang` varchar(200) NOT NULL,
  `sodienthoai` varchar(20) NOT NULL,
  `email` varchar(200) DEFAULT NULL,
  `diachi` text DEFAULT NULL,
  `phuongthuctt` varchar(50) DEFAULT NULL,
  `tongtien` decimal(15,0) DEFAULT 0,
  `ngaydat` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `donhang`
--

INSERT INTO `donhang` (`id`, `id_nguoidung`, `tenkhachhang`, `sodienthoai`, `email`, `diachi`, `phuongthuctt`, `tongtien`, `ngaydat`) VALUES
(8, 1, 'Phuc', '0353395733', 'phuclun1722@gmaill.com', '123', 'COD', 4100000, '2025-11-28 04:48:06'),
(9, 2, 'Son', '123', '123@gmail.com', '123', 'COD', 4100000, '2025-11-30 14:46:51'),
(10, 2, 'Nguyen Thi Tu Phuong', '0926197387', 'phuong2005@gmail.com', 'HN', 'COD', 4100000, '2025-11-30 15:10:19'),
(11, 2, 'phuong', '0926197387', 'phuong2005@gmail.com', 'Ha Noi', 'COD', 10000000, '2025-12-01 02:25:20'),
(12, 2, 'phuong tu', '0926197387', 'thuong@gmail.com', 'ha tinh', 'CHUYEN_KHOAN', 4100000, '2025-12-01 06:05:42');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `loaisanpham`
--

CREATE TABLE `loaisanpham` (
  `id` int(11) NOT NULL,
  `tenloaisanpham` varchar(200) NOT NULL,
  `hinhanhloaisanpham` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Đang đổ dữ liệu cho bảng `loaisanpham`
--

INSERT INTO `loaisanpham` (`id`, `tenloaisanpham`, `hinhanhloaisanpham`) VALUES
(1, 'Giày nữ chính hãng', 'https://bizweb.dktcdn.net/thumb/medium/100/347/092/products/ec0a3693f5f674922d16bebb09a002680d7d811b-787-1024-jpeg.jpg?v=1764145574553'),
(2, 'Giày nam chính hãng', 'https://bizweb.dktcdn.net/thumb/medium/100/347/092/products/puma-kaos-puma-ferrari-race-sds-tee-puma-black-630219-01-full10-k245o0v7.jpg?v=1763638787990');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sanpham`
--

CREATE TABLE `sanpham` (
  `id` int(11) NOT NULL,
  `tensanpham` varchar(200) NOT NULL,
  `giasanpham` int(15) NOT NULL,
  `hinhanhsanpham` varchar(200) NOT NULL,
  `motasanpham` varchar(10000) NOT NULL,
  `idsanpham` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Đang đổ dữ liệu cho bảng `sanpham`
--

INSERT INTO `sanpham` (`id`, `tensanpham`, `giasanpham`, `hinhanhsanpham`, `motasanpham`, `idsanpham`) VALUES
(1, 'Giày nam năng động', 4100000, 'https://bizweb.dktcdn.net/thumb/1024x1024/100/347/092/products/hf1233-105-3.jpg?v=1761948269350', 'Giày dép ngày nay không chỉ là vật dụng bảo vệ đôi chân mà còn là phụ kiện thể hiện phong cách của mỗi người. Với thiết kế hiện đại, chất liệu nhẹ và bền, chúng mang lại cảm giác thoải mái khi di chuyển. Màu sắc và kiểu dáng đa dạng giúp người dùng dễ dàng phối hợp với nhiều trang phục khác nhau. Đế giày được cải tiến để tăng độ bám và hạn chế trơn trượt, phù hợp cho cả đi học, đi làm và đi chơi. Đây là lựa chọn tiện lợi dành cho những ai muốn kết hợp sự thoải mái và thời trang trong từng bước chân.', 1),
(2, 'Giày Nike nam', 4100000, 'https://bizweb.dktcdn.net/thumb/1024x1024/100/347/092/products/aurora-hm9594-102-phslh001-2000-2048x2048.jpg?v=1764236888070', 'Giày da nam công sở thể hiện sự lịch lãm và chuyên nghiệp. Chúng thường được làm từ da thật, mang lại độ bền cao và vẻ sang trọng. Thiết kế trơn hoặc có họa tiết nhẹ giúp giày phù hợp với nhiều môi trường làm việc. Đế giày chắc chắn, tạo cảm giác tự tin khi bước đi. Những đôi giày này thường đi kèm với quần âu, vest hoặc sơ mi để hoàn thiện phong thái quý ông. Màu đen và nâu là hai tông phổ biến nhất. Đây là lựa chọn không thể thiếu cho những buổi gặp đối tác hoặc sự kiện trang trọng.', 1),
(3, 'Giày Adidas Chính Hãng - Lightblaze - Màu Đen/Hồng | JapanSport JR1299', 4100000, 'https://bizweb.dktcdn.net/thumb/1024x1024/100/347/092/products/190101010001-17-1.jpg?v=1764234669173', 'Giày chạy bộ nam được thiết kế để tối ưu sức bền và bảo vệ đôi chân. Phần thân giày thường sử dụng vật liệu siêu nhẹ giúp người dùng di chuyển linh hoạt. Đế giày được trang bị công nghệ chống sốc giúp hạn chế chấn thương khi tập luyện. Bề mặt lưới thoáng khí giữ cho bàn chân luôn khô thoáng. Kiểu dáng thể thao mạnh mẽ phù hợp với các chàng trai yêu vận động. Giày chạy bộ còn có khả năng bám đường tốt, hỗ trợ di chuyển trên nhiều bề mặt. Đây là người bạn đồng hành lý tưởng cho những người thích rèn luyện sức khỏe.', 1),
(4, 'Giày Adidas Chính Hãng - 4DFWD 4 Men\'s - Màu Đen | JapanSport JI1452', 4100000, 'https://bizweb.dktcdn.net/thumb/1024x1024/100/347/092/products/s-l1600-1-c8950e1d-0699-467c-8959-dbe15fc74b78.jpg?v=1764234140510', 'Giày lười nam là lựa chọn hoàn hảo cho những ai thích sự tiện lợi. Thiết kế không dây giúp việc mang vào và tháo ra trở nên cực kỳ nhanh chóng. Form giày ôm chân nhưng vẫn tạo cảm giác thoải mái khi di chuyển. Chúng thường được làm từ da mềm hoặc da lộn, mang đến vẻ sang trọng tinh tế. Giày lười phù hợp khi đi làm, gặp bạn bè hoặc dạo phố. Dù kết hợp với quần tây hay quần jean đều tạo nên phong cách lịch lãm. Đây là dạng giày được nhiều nam giới lựa chọn trong các sự kiện nhẹ nhàng.', 1),
(5, 'Giày Lacoste Chính hãng - Carnaby Cup Leather Sneakers - Màu Be | JapanSport 7-48SMA0021-4Y0', 4500000, 'https://bizweb.dktcdn.net/thumb/1024x1024/100/347/092/products/g-t-plus-hustle-plus-academy-plus-ep-11zon.jpg?v=1764233772213', 'Sandal nam mang đến sự mát mẻ và thoải mái trong những ngày thời tiết nóng bức. Thiết kế dây quai chắc chắn giúp ôm chân và giữ giày không bị tuột. Phần đế xốp hoặc cao su mềm hỗ trợ bước đi nhẹ nhàng. Sandal phù hợp cho các hoạt động ngoài trời như du lịch, picnics hoặc đi biển. Nhiều mẫu còn có khả năng chống trơn trượt, tăng độ an toàn. Phong cách đơn giản nhưng nam tính khiến sandal vẫn được ưa chuộng trong nhiều năm. Đây là sản phẩm tiện dụng cho mùa hè.', 1),
(6, 'Giày Puma Nam Chính Hãng - Scuderia Ferrari Caven 2.0 - Màu Đen | JapanSport 308901-01', 1000000, 'https://bizweb.dktcdn.net/thumb/1024x1024/100/347/092/products/giay-nike-vomero-18-hm6803-402-1-jpeg.jpg', 'Dép lê nam là phụ kiện quen thuộc trong cuộc sống hàng ngày. Thiết kế tối giản với quai ngang giúp mang vào nhanh và dễ sử dụng. Chất liệu thường là cao su hoặc EVA mềm nhẹ, mang lại sự thoải mái cho bàn chân. Dép lê phù hợp để đi trong nhà, đi siêu thị hoặc các hoạt động nhẹ. Một số mẫu được thiết kế thời trang, có thể đi ra ngoài mà vẫn giữ phong cách cá tính. Sự tiện lợi khiến dép lê trở thành item phổ biến không thể thiếu. Đây là lựa chọn hoàn hảo cho những lúc cần sự nhanh gọn.', 1),
(7, 'Giày Reebok Nam Chính Hãng - Royal Turbo Impulse Evo - Màu Đen | Japansport FU7193', 6000000, 'https://bizweb.dktcdn.net/thumb/1024x1024/100/347/092/products/b68f454b-be51-4c1a-8b76-529abce52b9b.jpg', 'Giày boots nam mang đến vẻ ngoài mạnh mẽ và nam tính. Được làm từ chất liệu da dày hoặc da lộn giúp giày trở nên chắc chắn. Thiết kế cổ cao bảo vệ cổ chân và tạo cảm giác an toàn khi di chuyển. Đế giày thường có rãnh sâu giúp bám đường tốt. Giày boots phù hợp với những outfit mùa đông hoặc phong cách bụi bặm. Khi kết hợp với quần jean và áo khoác, bạn sẽ trông cực kỳ phong trần. Đây là kiểu giày dành cho những chàng trai thích sự cá tính.', 1),
(8, 'Giày Mizuno Chính hãng - Wave Exceed Tour 5 AC - Màu Trắng Xanh | JapanSport 61GA227030', 900000, 'https://bizweb.dktcdn.net/thumb/1024x1024/100/347/092/products/847d7d67-1f85-4c0f-8afd-7e088b4dc9b8.jpg', 'Giày Oxford là biểu tượng của sự thanh lịch trong thời trang nam. Chúng có thiết kế buộc dây kín, tạo nên form dáng gọn gàng. Chất liệu da bóng hoặc da mịn giúp đôi giày trông sang trọng. Oxford thường được sử dụng trong các dịp trang trọng như tiệc cưới, hội nghị hoặc buổi thuyết trình quan trọng. Màu sắc phổ biến là đen, nâu và burgundy. Khi kết hợp cùng vest, Oxford giúp nâng tầm phong thái của người mang. Đây là kiểu giày đậm chất quý ông cổ điển.', 1),
(43, 'Giày Nike Nữ Chính Hãng - Pegasus 41 - Màu Rose Gold | JapanSport FD2723-604', 4000000, 'https://bizweb.dktcdn.net/thumb/medium/100/347/092/products/5b4fbc36-5f79-47a7-8920-780a8a981294.jpg?v=1761790161543', 'Sneaker nữ mang đến phong cách năng động, trẻ trung và rất dễ phối đồ. Thiết kế nhẹ, đế êm giúp di chuyển cả ngày mà không bị đau chân. Chất liệu vải lưới thoáng khí, giúp bàn chân luôn khô thoáng. Sneaker phù hợp với mọi hoạt động hằng ngày, từ đi học, đi làm cho đến đi chơi. Nhiều kiểu dáng thời trang giúp các bạn nữ dễ dàng thể hiện cá tính. Màu pastel cũng là xu hướng được nhiều bạn yêu thích. Đây là đôi giày không thể thiếu trong tủ đồ của phái đẹp.', 2),
(44, 'Giày Nike Nam Nữ Chính Hãng - Vomero 18 GS - Màu đen / đỏ | JapanSport HQ2157-007', 5000000, 'https://giaynhatchinhhang.vn/nike-vomero-18-gs-hq2157-007#', 'Sandal nữ mang lại sự thoáng mát và dễ chịu trong những ngày thời tiết nóng bức. Thiết kế quai mảnh hoặc quai bản lớn giúp ôm chân chắc chắn. Phần đế mềm giúp bước đi nhẹ nhàng và tự nhiên. Sandal có thể kết hợp cùng váy suông, quần short hoặc đầm maxi đều rất đẹp. Các hãng thời trang thường bổ sung chi tiết đá, hoa hoặc dây đan để tạo điểm nhấn. Sự tiện lợi khiến sandal trở thành lựa chọn lý tưởng cho những buổi dạo phố. Đây là đôi giày mang phong cách nữ tính nhưng không kém phần hiện đại.', 2),
(45, 'Giày Nike Nam Nữ Chính Hãng - Vomero 18 GS - Màu trắng / Xanh | JapanSport HQ2157-106', 750000, 'https://bizweb.dktcdn.net/thumb/medium/100/347/092/products/hq2157-007-1.jpg?v=1761422267463', 'Giày búp bê mang đến vẻ ngọt ngào và duyên dáng cho phái đẹp. Thiết kế mũi tròn mềm mại giúp bàn chân trông nhỏ nhắn hơn. Chất liệu da mềm hoặc vải tạo cảm giác thoải mái suốt ngày dài. Đế thấp phù hợp cho việc đi học, đi làm hoặc di chuyển nhiều. Búp bê dễ phối với váy, quần jean hoặc quần âu. Các chi tiết nơ, hoa hay hạt ngọc giúp đôi giày thêm phần nữ tính. Đây là kiểu giày luôn được nhiều bạn nữ yêu thích.', 2),
(46, 'Dép Nike Chính Hãng - Nike Calm - Màu Xám | JapanSport FD4116-004', 500000, 'https://bizweb.dktcdn.net/thumb/medium/100/347/092/products/giay-nike-vomero-18-gs-hq2157-106-1.jpg?v=1761408825440', 'Sandal cao gót nữ là sự kết hợp giữa vẻ thanh lịch và thoáng mát. Quai mảnh hoặc quai đan tạo nên sự tinh tế, tôn lên bàn chân thon gọn. Gót cao giúp dáng người thêm uyển chuyển và sang trọng. Màu nude, trắng hoặc đen là những lựa chọn dễ phối đồ nhất. Phần đế mềm hỗ trợ bước đi nhẹ nhàng hơn. Sandal cao gót phù hợp cho các buổi tiệc, hẹn hò hoặc sự kiện quan trọng. Đây là mẫu giày dành cho những ai muốn nổi bật một cách tinh tế.', 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `ten` varchar(200) NOT NULL,
  `sodienthoai` varchar(20) DEFAULT NULL,
  `diachi` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `ten`, `sodienthoai`, `diachi`) VALUES
(1, 'phuclun1722@gmail.com', '$2y$10$tJ9XSMuxt3WN3ZitfNa9buI9S9/SYewI92pgnOmCwvEl5xIujcFf.', 'Le Bao Phuc', '0353395733', NULL),
(2, 'phuong2005@gmail.com', '$2y$10$Gk6sCpwJYQiFLB/YJSK3YusRqvPZYCMs9sAFqChCOKpm/JdPGAzYS', 'Nguyen Thi Tu Phuong', '0926197387', NULL);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_madonhang` (`madonhang`);

--
-- Chỉ mục cho bảng `donhang`
--
ALTER TABLE `donhang`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_id_nguoidung` (`id_nguoidung`),
  ADD KEY `idx_sodienthoai` (`sodienthoai`);

--
-- Chỉ mục cho bảng `loaisanpham`
--
ALTER TABLE `loaisanpham`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_username` (`username`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT cho bảng `donhang`
--
ALTER TABLE `donhang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT cho bảng `loaisanpham`
--
ALTER TABLE `loaisanpham`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
