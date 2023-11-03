package com.utils.io;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.utils.log.Logger;

public final class IoTestUtils {

	private IoTestUtils() {
	}

	public static void checkFolderContentsEqual(
			final String folderPathString,
			final String otherFolderPathString,
			final List<FileCompareData> fileCompareDataList) throws Exception {

		Logger.printNewLine();
		Logger.printProgress("comparing folder contents:");
		Logger.printLine(folderPathString);
		Logger.printLine(otherFolderPathString);

		final List<String> filePathStringList = new ArrayList<>();
		final boolean folderExists = IoUtils.directoryExists(folderPathString);
		if (folderExists) {

			ListFileUtils.visitFilesRecursively(folderPathString,
					dirPath -> {
					},
					filePath -> {
						final String filePathString = filePath.toString();
						filePathStringList.add(filePathString);
					});
		}

		final Set<String> matchedOtherFilePathStringSet = new HashSet<>();
		for (final String filePathString : filePathStringList) {

			final String relativePathString =
					PathUtils.computeRelativePath(folderPathString, filePathString);
			final String otherFilePathString =
					PathUtils.computePath(otherFolderPathString, relativePathString);
			matchedOtherFilePathStringSet.add(otherFilePathString);

			final boolean contentEquals =
					FileUtils.contentEquals(new File(filePathString), new File(otherFilePathString));
			if (!contentEquals) {
				Logger.printWarning("The following files are different:" +
						System.lineSeparator() + filePathString +
						System.lineSeparator() + otherFilePathString);
			}

			final FileCompareData fileCompareData = new FileCompareData(
					filePathString, otherFilePathString, contentEquals);
			fileCompareDataList.add(fileCompareData);
		}

		final List<String> otherFilePathStringList = new ArrayList<>();
		final boolean otherFolderExists = IoUtils.directoryExists(otherFolderPathString);
		if (otherFolderExists) {

			ListFileUtils.visitFilesRecursively(otherFolderPathString,
					dirPath -> {
					},
					filePath -> {
						final String filePathString = filePath.toString();
						otherFilePathStringList.add(filePathString);
					});
		}

		for (final String otherFilePathString : otherFilePathStringList) {

			if (!matchedOtherFilePathStringSet.contains(otherFilePathString)) {

				final String relativePathString =
						PathUtils.computeRelativePath(otherFolderPathString, otherFilePathString);
				final String filePathString = PathUtils.computePath(folderPathString, relativePathString);

				final boolean contentEquals =
						FileUtils.contentEquals(new File(filePathString), new File(otherFilePathString));
				if (!contentEquals) {
					Logger.printWarning("The following files are different:" +
							System.lineSeparator() + filePathString +
							System.lineSeparator() + otherFilePathString);
				}

				final FileCompareData fileCompareData = new FileCompareData(
						filePathString, otherFilePathString, contentEquals);
				fileCompareDataList.add(fileCompareData);
			}
		}
	}
}
