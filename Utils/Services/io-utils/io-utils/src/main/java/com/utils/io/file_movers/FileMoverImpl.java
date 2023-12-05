package com.utils.io.file_movers;

import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import com.utils.io.IoUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;
import com.utils.log.Logger;

class FileMoverImpl implements FileMover {

	FileMoverImpl() {
	}

	@Override
	public boolean moveFile(
			final String srcFilePathString,
			final String dstFilePathString,
			final boolean verboseProgress,
			final boolean verboseError) {

		boolean success = false;
		try {
			if (verboseProgress) {

				Logger.printProgress("moving file:");
				Logger.printLine(srcFilePathString);
				Logger.printLine("to:");
				Logger.printLine(dstFilePathString);
			}

			final boolean keepGoing;
			if (IoUtils.fileExists(dstFilePathString)) {
				keepGoing = FactoryReadOnlyFlagClearer.getInstance()
						.clearReadOnlyFlagFileNoChecks(dstFilePathString, false, verboseError);
			} else {
				keepGoing = FactoryFolderCreator.getInstance()
						.createParentDirectories(dstFilePathString, false, verboseError);
			}
			if (keepGoing) {

				final List<CopyOption> copyOptionList = new ArrayList<>();
				copyOptionList.add(StandardCopyOption.REPLACE_EXISTING);
				final CopyOption[] copyOptionArray = copyOptionList.toArray(new CopyOption[0]);

				final Path srcFilePath = Paths.get(srcFilePathString);
				final Path dstFilePath = Paths.get(dstFilePathString);
				Files.move(srcFilePath, dstFilePath, copyOptionArray);

				success = true;
			}

		} catch (final Exception exc) {
			Logger.printException(exc);
		}

		if (!success) {
			if (verboseError) {
				Logger.printError("failed to move file " +
						System.lineSeparator() + srcFilePathString +
						System.lineSeparator() + "to:" +
						System.lineSeparator() + dstFilePathString);
			}
		}

		return success;
	}
}
