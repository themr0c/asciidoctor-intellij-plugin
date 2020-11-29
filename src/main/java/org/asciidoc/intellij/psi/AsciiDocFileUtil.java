package org.asciidoc.intellij.psi;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AsciiDocFileUtil {

  public static List<AsciiDocSection> findSections(Project project, String key) {
    if (key.length() == 0) {
      return Collections.emptyList();
    }
    List<AsciiDocSection> result = null;
    final GlobalSearchScope scope = GlobalSearchScope.projectScope(project);
    Collection<AsciiDocSection> asciiDocSections = AsciiDocSectionKeyIndex.getInstance().get(key, project, scope);
    ProjectFileIndex index = ProjectRootManager.getInstance(project).getFileIndex();
    for (AsciiDocSection asciiDocSection : asciiDocSections) {
      VirtualFile virtualFile = asciiDocSection.getContainingFile().getVirtualFile();
      if (index.isInLibrary(virtualFile)
        || index.isExcluded(virtualFile)
        || index.isInLibraryClasses(virtualFile)
        || index.isInLibrarySource(virtualFile)) {
        continue;
      }
      if (key.equals(asciiDocSection.getTitle()) || (asciiDocSection.matchesAutogeneratedId(key) && asciiDocSection.getBlockId() == null)) {
        if (result == null) {
          result = new ArrayList<>();
        }
        result.add(asciiDocSection);
      }
    }
    return result != null ? result : Collections.emptyList();
  }

  public static List<AsciiDocSection> findSections(Project project) {
    List<AsciiDocSection> result = new ArrayList<>();
    ProjectFileIndex index = ProjectRootManager.getInstance(project).getFileIndex();
    Collection<String> keys = AsciiDocSectionKeyIndex.getInstance().getAllKeys(project);
    final GlobalSearchScope scope = GlobalSearchScope.projectScope(project);
    for (String key : keys) {
      Collection<AsciiDocSection> asciiDocSections = AsciiDocSectionKeyIndex.getInstance().get(key, project, scope);
      for (AsciiDocSection asciiDocSection : asciiDocSections) {
        VirtualFile virtualFile = asciiDocSection.getContainingFile().getVirtualFile();
        if (index.isInLibrary(virtualFile)
          || index.isExcluded(virtualFile)
          || index.isInLibraryClasses(virtualFile)
          || index.isInLibrarySource(virtualFile)) {
          continue;
        }
        result.add(asciiDocSection);
      }
    }
    return result;
  }
}
