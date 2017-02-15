// Copyright (C) 2016 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.gerrit.server.schema;

import com.google.gerrit.reviewdb.server.ReviewDb;
import com.google.gerrit.server.GerritPersonIdent;
import com.google.gerrit.server.account.externalids.ExternalId;
import com.google.gerrit.server.account.externalids.ExternalIdReader;
import com.google.gerrit.server.account.externalids.ExternalIdsUpdate;
import com.google.gerrit.server.config.AllUsersName;
import com.google.gerrit.server.git.GitRepositoryManager;
import com.google.gwtorm.server.OrmException;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import org.eclipse.jgit.errors.ConfigInvalidException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.notes.NoteMap;
import org.eclipse.jgit.revwalk.RevWalk;

public class Schema_144 extends SchemaVersion {
  private static final String COMMIT_MSG = "Import external IDs from ReviewDb";

  private final GitRepositoryManager repoManager;
  private final AllUsersName allUsersName;
  private final PersonIdent serverIdent;

  @Inject
  Schema_144(
      Provider<Schema_143> prior,
      GitRepositoryManager repoManager,
      AllUsersName allUsersName,
      @GerritPersonIdent PersonIdent serverIdent) {
    super(prior);
    this.repoManager = repoManager;
    this.allUsersName = allUsersName;
    this.serverIdent = serverIdent;
  }

  @Override
  protected void migrateData(ReviewDb db, UpdateUI ui) throws OrmException, SQLException {
    Set<ExternalId> toAdd = ExternalId.from(db.accountExternalIds().all().toList());
    try {
      try (Repository repo = repoManager.openRepository(allUsersName);
          RevWalk rw = new RevWalk(repo);
          ObjectInserter ins = repo.newObjectInserter()) {
        ObjectId rev = ExternalIdReader.readRevision(repo);

        NoteMap noteMap = ExternalIdReader.readNoteMap(rw, rev);

        for (ExternalId extId : toAdd) {
          ExternalIdsUpdate.upsert(rw, ins, noteMap, extId);
        }

        ExternalIdsUpdate.commit(repo, rw, ins, rev, noteMap, COMMIT_MSG, serverIdent, serverIdent);
      }
    } catch (IOException | ConfigInvalidException e) {
      throw new OrmException("Failed to migrate external IDs to NoteDb", e);
    }
  }
}
