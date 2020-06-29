-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le : lun. 29 juin 2020 à 12:22
-- Version du serveur :  10.3.22-MariaDB-0+deb10u1
-- Version de PHP : 7.3.14-1~deb10u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `JRoomDB`
--

-- --------------------------------------------------------

--
-- Structure de la table `attendance`
--

CREATE TABLE `attendance` (
  `conference_id` int(11) NOT NULL,
  `guest_attendee_guid` varchar(36) NOT NULL,
  `registered_attendee_id` int(11) NOT NULL,
  `last_activity_date` datetime DEFAULT current_timestamp(),
  `access_token` varchar(100) NOT NULL,
  `voice_stream_id` int(11) DEFAULT NULL,
  `camera_stream_id` int(11) DEFAULT NULL,
  `screen_stream_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `attendance`
--

INSERT INTO `attendance` (`conference_id`, `guest_attendee_guid`, `registered_attendee_id`, `last_activity_date`, `access_token`, `voice_stream_id`, `camera_stream_id`, `screen_stream_id`) VALUES
(700000002, '0', 5, NULL, '5ADAACCD9A01184BFED6253DB4EA6867698A13E8F14EDB9F99', 7, 9, 8),
(700000002, '13864340-30c9-4bcf-b615-de9efe63cbcb', 0, NULL, 'BD9904605C5C2925424C07167EAF8999DAC55B4AAA6F566524', 63, 62, 61),
(700000002, '27034e17-d0f7-4aa2-a414-cb470496e2fd', 0, NULL, 'D9CF9F8172AAB7E68A0029A613400F85998237E60BC35DD0F9', 64, 65, 66),
(700000002, '3d83ff35-d01f-415a-a4af-e8f6d5d18935', 0, NULL, '6410D8E815ACF808BD80601FE732849B717294F044226FC39B', 75, 74, 73),
(700000002, '5caec552-876b-4290-87fb-e15c5102f191', 0, NULL, 'F31FA1826588E081BF5980ABC521DAB948FC3682B8C41EDC90', 60, 58, 59),
(700000002, '72cfbae9-f719-4d4e-b401-0a955bb63aa4', 0, NULL, 'B4872D2923E5856343DBC85D56285CDA93F2053809E158EF9A', 51, 50, 49),
(700000002, '8b51329f-2cef-4f84-8283-a026d1ca7751', 0, NULL, 'E996AC21FC724C9CCB8E9CA42E28BC5FAA273AF3783AFC8EF0', 68, 69, 67),
(700000002, 'dced61a8-ff4f-45e7-b840-3dcf02335979', 0, NULL, 'A61591FB5768CDBE67293B08FAD6FCC51BEE57F932FB9B4C78', 71, 72, 70),
(700000002, 'f049cce5-2206-4f7f-9ad1-b64a27d04a52', 0, NULL, 'BCB869B08190AF89E3CAC228DE3A73D23BAA7D53B5BDFCCBC6', 57, 56, 55),
(700000003, '0', 7, NULL, '6AFD6D5E4CAFCE93F66CD8442CD65AFC68D8897FC6CC89A8E5', 76, 78, 77),
(700000003, '07757b71-5b13-4946-bf18-d70d70ac698c', 0, NULL, 'E93FD3453BE877C275971423840540E1F84CD3D05434706B5C', 86, 85, 87),
(700000003, '46b4cc1b-9937-4ea3-9514-c68607b4d668', 0, NULL, 'F86A07951FF8572C6B68E086CB23B8EDD8C5098009B7768076', 83, 84, 82),
(700000003, 'a0faf686-eaa6-4bf9-aa1c-990ebfb92c83', 0, NULL, 'A6D8B503CB8FDFAFACFC49A95D17F579B91E66591AE5A08B9E', 81, 79, 80),
(700000004, '0', 8, NULL, '38E35B85C96A25F54D2EE88F6815EE53326932C7A91909E19B', 89, 88, 90),
(700000004, '239c6796-6bbe-4755-b92f-8211457a0cd5', 0, NULL, '89A4BE86E1A6F1ADC27B25C4CE2D0B1C4AD2950040BE49D7E9', 92, 91, 93);

-- --------------------------------------------------------

--
-- Structure de la table `avatar`
--

CREATE TABLE `avatar` (
  `id` int(11) NOT NULL,
  `image_blob` mediumblob DEFAULT NULL,
  `x` int(11) NOT NULL,
  `y` int(11) NOT NULL,
  `width` int(11) NOT NULL,
  `height` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `chat`
--

CREATE TABLE `chat` (
  `discussion_id` int(11) NOT NULL,
  `message_id` int(11) NOT NULL,
  `sender_attendee_id` int(11) NOT NULL,
  `receiver_attendee_id` int(11) NOT NULL,
  `sent_date` datetime NOT NULL,
  `deleted` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `conference`
--

CREATE TABLE `conference` (
  `id` int(11) NOT NULL,
  `password` mediumint(9) NOT NULL,
  `owner_id` int(11) DEFAULT NULL,
  `discussion_id` int(11) DEFAULT NULL,
  `registered_animator_id` int(11) DEFAULT NULL,
  `guest_animator_guid` varchar(36) DEFAULT NULL,
  `active` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `conference`
--

INSERT INTO `conference` (`id`, `password`, `owner_id`, `discussion_id`, `registered_animator_id`, `guest_animator_guid`, `active`) VALUES
(700000000, 123456, NULL, NULL, NULL, NULL, 1),
(700000001, 499753, NULL, NULL, NULL, NULL, 1),
(700000002, 253780, 5, NULL, 5, NULL, 1),
(700000003, 120635, 7, NULL, 7, NULL, 1),
(700000004, 821801, 8, NULL, 8, NULL, 1);

-- --------------------------------------------------------

--
-- Structure de la table `data_stream`
--

CREATE TABLE `data_stream` (
  `id` int(11) NOT NULL,
  `conference_id` int(11) DEFAULT NULL,
  `guest_attendee_guid` varchar(36) DEFAULT NULL,
  `registered_attendee_id` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `data` mediumblob DEFAULT NULL,
  `format` varchar(50) DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  `height` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `data_stream`
--

INSERT INTO `data_stream` (`id`, `conference_id`, `guest_attendee_guid`, `registered_attendee_id`, `type`, `data`, `format`, `width`, `height`) VALUES
(7, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(8, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(9, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(11, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(12, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(13, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(14, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(15, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(16, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(18, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(19, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(20, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(21, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(22, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(23, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(24, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(25, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(26, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(27, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(28, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(29, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(30, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(31, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(32, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(33, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(34, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(35, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(36, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(37, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(38, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(39, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(40, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(41, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(42, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(43, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(44, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(45, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(46, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(47, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(48, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(49, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(50, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(51, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(52, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(53, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(54, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(55, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(56, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(57, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(58, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(59, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(60, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(61, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(62, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(63, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(64, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(65, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(66, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(67, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(68, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(69, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(70, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(71, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(72, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(73, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(74, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(75, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(76, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(77, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(78, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(79, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(80, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(81, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(82, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(83, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(84, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(85, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(86, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(87, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(88, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(89, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(90, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(91, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(92, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(93, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `discussion`
--

CREATE TABLE `discussion` (
  `id` int(11) NOT NULL,
  `description` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `guest_attendee`
--

CREATE TABLE `guest_attendee` (
  `guid` varchar(36) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `last_known_ip` varchar(15) DEFAULT NULL,
  `last_known_client` varchar(100) DEFAULT NULL,
  `last_activity_date` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `guest_attendee`
--

INSERT INTO `guest_attendee` (`guid`, `name`, `last_known_ip`, `last_known_client`, `last_activity_date`) VALUES
('0', 'DO_NOT_USE', NULL, NULL, '2020-06-12 16:25:13'),
('07757b71-5b13-4946-bf18-d70d70ac698c', NULL, NULL, NULL, NULL),
('13864340-30c9-4bcf-b615-de9efe63cbcb', NULL, NULL, NULL, NULL),
('239c6796-6bbe-4755-b92f-8211457a0cd5', NULL, NULL, NULL, NULL),
('27034e17-d0f7-4aa2-a414-cb470496e2fd', NULL, NULL, NULL, NULL),
('3d83ff35-d01f-415a-a4af-e8f6d5d18935', NULL, NULL, NULL, NULL),
('46b4cc1b-9937-4ea3-9514-c68607b4d668', NULL, NULL, NULL, NULL),
('5caec552-876b-4290-87fb-e15c5102f191', NULL, NULL, NULL, NULL),
('72cfbae9-f719-4d4e-b401-0a955bb63aa4', NULL, NULL, NULL, NULL),
('8b51329f-2cef-4f84-8283-a026d1ca7751', NULL, NULL, NULL, NULL),
('a0faf686-eaa6-4bf9-aa1c-990ebfb92c83', NULL, NULL, NULL, NULL),
('dced61a8-ff4f-45e7-b840-3dcf02335979', NULL, NULL, NULL, NULL),
('f049cce5-2206-4f7f-9ad1-b64a27d04a52', NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `message`
--

CREATE TABLE `message` (
  `id` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `content` varchar(200) NOT NULL,
  `metadata` varchar(50) NOT NULL,
  `digest` varchar(40) NOT NULL,
  `av_scan_result` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `registered_attendee`
--

CREATE TABLE `registered_attendee` (
  `id` int(11) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `avatar_id` int(11) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `last_known_ip` tinytext DEFAULT NULL,
  `last_known_client` tinytext DEFAULT NULL,
  `last_activity_date` datetime DEFAULT current_timestamp(),
  `personal_conference_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `registered_attendee`
--

INSERT INTO `registered_attendee` (`id`, `username`, `password`, `email`, `avatar_id`, `name`, `last_known_ip`, `last_known_client`, `last_activity_date`, `personal_conference_id`) VALUES
(0, 'DO_NOT_USE', 'DO_NOT_USE', 'DO_NOT_USE', NULL, NULL, NULL, NULL, '2020-06-12 16:23:03', NULL),
(1, NULL, 'sphere', 'sphere@caporal7.com', NULL, NULL, NULL, NULL, NULL, 700000000),
(4, NULL, 'defconf', 'defconf@caporal7.com', NULL, NULL, NULL, NULL, NULL, 700000001),
(5, NULL, 'caporal', 'caporal@caporal7.com', NULL, NULL, NULL, NULL, NULL, 700000002),
(7, NULL, '123456', 'ayoubismaili1@gmail.com', NULL, NULL, NULL, NULL, NULL, 700000003),
(8, NULL, 'testbin', 'testbin', NULL, NULL, NULL, NULL, NULL, 700000004);

-- --------------------------------------------------------

--
-- Structure de la table `session`
--

CREATE TABLE `session` (
  `id` int(11) NOT NULL,
  `registered_attendee_id` int(11) NOT NULL,
  `cookie` varchar(50) NOT NULL,
  `last_activity_date` datetime DEFAULT current_timestamp(),
  `expired` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `session`
--

INSERT INTO `session` (`id`, `registered_attendee_id`, `cookie`, `last_activity_date`, `expired`) VALUES
(1, 1, '482BD958D9C541B64F57326741D56EF2830FF47A2B9EE66A12', NULL, 0),
(2, 1, '66C9310C50D4939A2DD9FB25C411A27640DB08223389AE1437', NULL, 0),
(3, 1, 'DFAB7314A2B18521AD67BA7430D39406E0827BA6008F60F78E', NULL, 0),
(5, 1, '379AF2B2F01BC2EFFF525916EC02FC029B19E1DAF824C07835', NULL, 0),
(6, 1, 'E827AE773368B6ECCA0D6159041C110CA8E8AAAAB9A1617B59', NULL, 0),
(7, 1, '374C645DB30E82B825D1706DEEAB8E6AF49EF5D0223E45F356', NULL, 0),
(8, 1, '65B5A3E19E7154D18796940D74912F636D7C5F7E71727CCF73', NULL, 0),
(9, 1, 'F50E0C0B272BB28086B4C1579BF7533EE6B4FCA48E7E0DAB1A', NULL, 0),
(10, 1, '0DB1826C0FC37068453F281A8BF8D737CA4798D84BBAD1F4CB', NULL, 0),
(11, 1, '9D94842D927361D82DBA15844B075AC6D12D98FE5F6FA7FA37', NULL, 0),
(12, 1, 'D2B60CFAEFDC22CC8BAD760579295614477EDE0800CD3027B1', NULL, 0),
(13, 1, '3079E309CFF0DAC0E0659148BFA59487CC2E974899EA913713', NULL, 0),
(14, 1, '5BBA723309ED57380D01012E94748E20D3501AA82E74E20AE0', NULL, 0),
(15, 1, 'B2F0230AE6224B1D5E8A9B3D394E741A2FDBFBE7BFF41097F7', NULL, 0),
(16, 1, 'FF0927FD06BBD89040654CA209A7594C26AC8CCFABFAE1CE94', NULL, 0),
(19, 1, 'DC977823A8A6726AE9B41A33843CBAC94E2C73B852EF8960FA', NULL, 0),
(20, 1, 'EF0BAE6E0FD7186C953EF4D9E9D3D04E79C3096E0EE12D3C7D', NULL, 0),
(21, 1, '9AE5D0150AE6CD694031D5A2E25CE81D659A64A14E8F44280D', NULL, 0),
(22, 1, '7155D77EE545B2CF2F43B787235EC0FF1365042AE4CCCC0255', NULL, 0),
(23, 1, '0D0029BB2FA390EA97DC7A53E0F60262EBC99EB0CB55F5533C', NULL, 0),
(24, 1, '0D073D14BF15790BAE0E488637CEAE25D8B1C75E87AF801DEB', NULL, 0),
(25, 4, '71B2F67357FFA06FBE1C66DD5F7EF672EE3A96CC8A0614E3EF', NULL, 0),
(26, 1, '45947EE176944D4B7194BC79CF2F7D2F1F31887C8858DFF15D', NULL, 0),
(27, 4, 'FD0ED41616DBF8B53826380E6D82442D64FE12EA42B11D4036', NULL, 0),
(28, 5, 'CF99ECF616BFFA8094B6F47E2A0C96684632F7192335EEBC94', NULL, 0),
(29, 7, '1FC6D68FB20125399D77CA91486318009A88C00DE21736B2D2', NULL, 0),
(30, 8, 'BE063F5B29EF4A9E7A0CF90BE67EA19894DBB5EB269912013C', NULL, 0);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `attendance`
--
ALTER TABLE `attendance`
  ADD PRIMARY KEY (`conference_id`,`guest_attendee_guid`,`registered_attendee_id`),
  ADD KEY `fk_attendance_guest_attendee_guid` (`guest_attendee_guid`),
  ADD KEY `fk_attendance_registered_attendee_id` (`registered_attendee_id`);

--
-- Index pour la table `avatar`
--
ALTER TABLE `avatar`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `chat`
--
ALTER TABLE `chat`
  ADD KEY `fk_chat_discussion_id` (`discussion_id`),
  ADD KEY `fk_chat_message_id` (`message_id`);

--
-- Index pour la table `conference`
--
ALTER TABLE `conference`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_conference_guest_attendee_guest_animator_guid` (`guest_animator_guid`),
  ADD KEY `fk_conference_registered_attendee_registered_animator_id` (`registered_animator_id`),
  ADD KEY `fk_conference_registered_attendee_owned_id` (`owner_id`),
  ADD KEY `fk_conference_discussion_id` (`discussion_id`);

--
-- Index pour la table `data_stream`
--
ALTER TABLE `data_stream`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_data_stream_attendance_multiple` (`conference_id`,`guest_attendee_guid`,`registered_attendee_id`);

--
-- Index pour la table `discussion`
--
ALTER TABLE `discussion`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `guest_attendee`
--
ALTER TABLE `guest_attendee`
  ADD PRIMARY KEY (`guid`);

--
-- Index pour la table `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `registered_attendee`
--
ALTER TABLE `registered_attendee`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `registered_attendee_email_index` (`email`),
  ADD UNIQUE KEY `registered_attendee_username_index` (`username`),
  ADD KEY `fk_registered_attendee_avatar_avatar_id` (`avatar_id`) USING BTREE,
  ADD KEY `fk_registered_attendee_conference_id` (`personal_conference_id`);

--
-- Index pour la table `session`
--
ALTER TABLE `session`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_session_registered_attendee_id` (`registered_attendee_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `avatar`
--
ALTER TABLE `avatar`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `conference`
--
ALTER TABLE `conference`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=700000005;

--
-- AUTO_INCREMENT pour la table `data_stream`
--
ALTER TABLE `data_stream`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=94;

--
-- AUTO_INCREMENT pour la table `discussion`
--
ALTER TABLE `discussion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `message`
--
ALTER TABLE `message`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `registered_attendee`
--
ALTER TABLE `registered_attendee`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pour la table `session`
--
ALTER TABLE `session`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `attendance`
--
ALTER TABLE `attendance`
  ADD CONSTRAINT `fk_attendance_conference_id` FOREIGN KEY (`conference_id`) REFERENCES `conference` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_attendance_guest_attendee_guid` FOREIGN KEY (`guest_attendee_guid`) REFERENCES `guest_attendee` (`guid`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_attendance_registered_attendee_id` FOREIGN KEY (`registered_attendee_id`) REFERENCES `registered_attendee` (`id`);

--
-- Contraintes pour la table `chat`
--
ALTER TABLE `chat`
  ADD CONSTRAINT `fk_chat_discussion_id` FOREIGN KEY (`discussion_id`) REFERENCES `discussion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_chat_message_id` FOREIGN KEY (`message_id`) REFERENCES `message` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `conference`
--
ALTER TABLE `conference`
  ADD CONSTRAINT `fk_conference_discussion_id` FOREIGN KEY (`discussion_id`) REFERENCES `discussion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_conference_guest_attendee_guest_animator_guid` FOREIGN KEY (`guest_animator_guid`) REFERENCES `guest_attendee` (`guid`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_conference_registered_attendee_owned_id` FOREIGN KEY (`owner_id`) REFERENCES `registered_attendee` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_conference_registered_attendee_registered_animator_id` FOREIGN KEY (`registered_animator_id`) REFERENCES `registered_attendee` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `data_stream`
--
ALTER TABLE `data_stream`
  ADD CONSTRAINT `fk_data_stream_attendance_multiple` FOREIGN KEY (`conference_id`,`guest_attendee_guid`,`registered_attendee_id`) REFERENCES `attendance` (`conference_id`, `guest_attendee_guid`, `registered_attendee_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `registered_attendee`
--
ALTER TABLE `registered_attendee`
  ADD CONSTRAINT `fk_registered_attendee_avatar_id` FOREIGN KEY (`avatar_id`) REFERENCES `avatar` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_registered_attendee_conference_id` FOREIGN KEY (`personal_conference_id`) REFERENCES `conference` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `session`
--
ALTER TABLE `session`
  ADD CONSTRAINT `fk_session_registered_attendee_id` FOREIGN KEY (`registered_attendee_id`) REFERENCES `registered_attendee` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
