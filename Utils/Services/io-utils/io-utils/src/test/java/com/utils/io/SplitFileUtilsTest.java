package com.utils.io;

import org.junit.jupiter.api.Test;

class SplitFileUtilsTest {

	@Test
	void testSplitFile() {

		final String filePathString;
		final long sizeOfChunk;
		final int input = Integer.parseInt("2");
		if (input == 1) {
			filePathString = "D:\\casdev\\td5\\da\\mda\\000\\DAMDA_000U0_000\\" +
					"_FS_DAMDA_000U0_NORMAL\\out\\code\\_dwarf\\FS_DAMDA_000U0_000.elf_DWARF.txt";
			sizeOfChunk = 400 * 1024 * 1024;

		} else if (input == 2) {
			filePathString = "D:\\tmp\\ProjectAnalyzer\\Outputs\\FS_DAMDA_000U0_000.txt";
			sizeOfChunk = 200 * 1024 * 1024;

		} else {
			throw new RuntimeException();
		}

		SplitFileUtils.splitFile(filePathString, sizeOfChunk);
	}
}
