package com.societegenerale.commons.plugin.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.societegenerale.commons.plugin.Log;
import org.apache.commons.io.FileUtils;
import org.mockito.Mock;

abstract class AbstractExcludePathTest
{
    private static final String fileComment                  = "//\n // This file is generated by X \n //";
    static final         String CLASS_NAME                   = "ExcludePathTestFile";
    static final         String CLASS_NAME_WITH_FILE_COMMENT = "ExcludePathTestFileWithFileComment";
    static final         String CONTENT_WITH_DEFAULT_PACKAGE = "import org.junit.Test; \r\n"
                                                               + "// line comment \r\n"
                                                               + "/**"
                                                               + "* Block comment "
                                                               + "*/"
                                                               + "public class " + CLASS_NAME
                                                               + " implements Serializable\r\n"
                                                               + "{ "
                                                               + "   public static class InnerClass{}"
                                                               + "}";

    private static final String CONTENT_WITH_FILE_COMMENT = "import org.junit.Test; \r\n"
                                                            + "// line comment, this is a genetated class foo \r\n"
                                                            + "/**"
                                                            + "* Block comment, this is a generated class bar "
                                                            + "*/\n"
                                                            + "public class " + CLASS_NAME_WITH_FILE_COMMENT
                                                            + "{ \r\n"
                                                            + "   public static class InnerClass{}"
                                                            + "}";

    private static final String CONTENT_WITH_CLASS_IN_COMMENT = "/** \r\n"
                                                            + "public class " + CLASS_NAME_WITH_FILE_COMMENT
                                                            + "{ \r\n"
                                                            + "   public static class InnerClass{}"
                                                            + "}"
                                                            + "*/\n";

    static final String PACKAGE_NAME         = AbstractExcludePathTest.class.getPackage().getName();
    static final String CONTENT_WITH_PACKAGE = "package " + PACKAGE_NAME + ";\r\n"
                                               + CONTENT_WITH_DEFAULT_PACKAGE;

    static final String CONTENT_WITH_FILE_COMMENT_AND_PACKAGE =
            fileComment + "package " + PACKAGE_NAME + ";\r\n"
            + CONTENT_WITH_FILE_COMMENT;

    static final String CONTENT_WITH_CLASS_IN_COMMENT_AND_PACKAGE =
            fileComment + "package " + PACKAGE_NAME + ";\r\n"
            + CONTENT_WITH_CLASS_IN_COMMENT;

    private static final String PROJECT_BUILD_DIR_NAME = "target";

    private static Path testTempRootDirectory          = null;
    private static Path testProjectBuildDirectory      = null;
    private static Path tempJavaFile                   = null;
    private static Path tempJavaFileWithFileComment    = null;
    private static Path tempJavaFileWithDefaultPackage = null;

    @Mock
    private Log logger;

    static void init() throws IOException
    {
        testTempRootDirectory = Files.createTempDirectory("AbstractExcludePathTestRoot");

        testProjectBuildDirectory = Paths.get(testTempRootDirectory.toString(), PROJECT_BUILD_DIR_NAME);
        Files.createDirectory(testProjectBuildDirectory);

        tempJavaFileWithDefaultPackage =
                Files.createFile(
                        Paths.get(testProjectBuildDirectory.toString(), CLASS_NAME + ".java"));
        Files.write(tempJavaFileWithDefaultPackage, CONTENT_WITH_DEFAULT_PACKAGE.getBytes());

        final Path packagePath = Files.createDirectories(Paths.get(testProjectBuildDirectory.toString(),
                                                                   AbstractExcludePathTest.class.getPackage()
                                                                                                .getName()
                                                                                                .split("\\.")));

        tempJavaFile = Files.createFile(
                Paths.get(packagePath.toString(), CLASS_NAME + ".java"));
        Files.write(tempJavaFile, CONTENT_WITH_PACKAGE.getBytes());

        tempJavaFileWithFileComment =
                Files.createFile(
                        Paths.get(packagePath.toString(),
                                  CLASS_NAME_WITH_FILE_COMMENT + ".java"));
        Files.write(tempJavaFileWithFileComment, CONTENT_WITH_FILE_COMMENT_AND_PACKAGE.getBytes());
    }

    static void cleanup() throws IOException
    {
        if (testTempRootDirectory != null)
        {
            FileUtils.forceDelete(testTempRootDirectory.toFile());
        }
    }

    Path getTempJavaFile()
    {
        return tempJavaFile;
    }

    static Path getTempJavaFileWithFileComment()
    {
        return tempJavaFileWithFileComment;
    }

    Path getTempJavaFileWithDefaultPackage()
    {
        return tempJavaFileWithDefaultPackage;
    }

    Log getLogger()
    {
        return logger;
    }

    Path getTestTempRootDirectory()
    {
        return testTempRootDirectory;
    }

    static Path getTestProjectBuildDirectory()
    {
        return testProjectBuildDirectory;
    }
}
