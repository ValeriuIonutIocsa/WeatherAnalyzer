package com.utils.io.file_copiers;

import com.utils.annotations.ApiMethod;

public interface FileCopier {

	@ApiMethod
	boolean copyFile(
			String srcFilePathString,
			String dstFilePathString,
			boolean copyAttributes,
			boolean verboseProgress,
			boolean verboseError);

	@ApiMethod
	boolean copyFileNoChecks(
			String srcFilePathString,
			String dstFilePathString,
			boolean dstFileExists,
			boolean copyAttributes,
			boolean verboseProgress,
			boolean verboseError);
}
