SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

CREATE TABLE 'custtbl'(
`ID` int(100) NOT NULL, 
`CivilStatus` varchar(500) NOT NULL,
`Fullname` varchar(500) NOT NULL, 
`Gender` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `custtbl` (`ID`, `Fullname`, `Gender`, `CivilStatus`) VALUES 
(1, 'Julie Ann Makilan', 'Female', 'Single'), 
(2, 'Anthony Gallego', 'Male', 'Married'); 

ALTER TABLE `custtbl` 
ADD PRIMARY KEY (`ID`);

ALTER TABLE `custtbl`
MODIFY `ID` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11; 
COMMIT;