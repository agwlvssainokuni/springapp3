-- NAME: getNumberOfWorkday
-- PARAM:
--   String    name
--   LocalDate fm
--   LocalDate to
SELECT
	COUNT(DISTINCT H0.dt)
FROM
	dayoff_master H0
WHERE
	H0.name = :name
	AND
	H0.dt BETWEEN :fm AND :to
;

-- NAME: getNextWorkday
-- PARAM:
--   String    name
--   LocalDate fm
--   int       numberOfWorkday
SELECT
	ADD_DAYS(:fm, D.nm)
FROM
	(SELECT A.d*10+B.d AS nm FROM digit A, digit B) D
WHERE
	NOT EXISTS (
		SELECT 1
		FROM
			dayoff_master H0
		WHERE
			H0.name = :name
			AND
			H0.dt = ADD_DAYS(:fm, D.nm)
	)
	AND
	(
		SELECT COUNT(DISTINCT H0.dt)
		FROM
			dayoff_master H0
		WHERE
			H0.name = :name
			AND
			H0.dt BETWEEN :fm AND ADD_DAYS(:fm, D.nm)
	) = D.nm - :numberOfWorkday + 1
;
