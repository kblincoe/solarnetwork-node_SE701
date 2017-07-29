SELECT 
	SYSSCHEMAS.SCHEMANAME, 
	SYSTABS.TABLENAME, 
	MAX(ESTIMSPACESAVING) AS ESTIMSPACESAVING
FROM
    SYS.SYSTABLES SYSTABS, 
    SYS.SYSSCHEMAS SYSSCHEMAS,
    TABLE (SYSCS_DIAG.SPACE_TABLE()) AS T2
WHERE SYSSCHEMAS.SCHEMANAME = ?
    AND SYSTABS.TABLETYPE = 'T'
    AND SYSSCHEMAS.SCHEMAID = SYSTABS.SCHEMAID
    AND SYSTABS.TABLEID = T2.TABLEID
    AND T2.ESTIMSPACESAVING > ?
GROUP BY SYSSCHEMAS.SCHEMANAME, SYSTABS.TABLENAME
ORDER BY MAX(ESTIMSPACESAVING) DESC