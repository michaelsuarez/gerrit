/**
 * @license
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import '@polymer/iron-icon/iron-icon.js';
import '../../../styles/shared-styles.js';
import '../../shared/gr-button/gr-button.js';
import '../../shared/gr-rest-api-interface/gr-rest-api-interface.js';
import {GestureEventListeners} from '@polymer/polymer/lib/mixins/gesture-event-listeners.js';
import {LegacyElementMixin} from '@polymer/polymer/lib/legacy/legacy-element-mixin.js';
import {PolymerElement} from '@polymer/polymer/polymer-element.js';
import {htmlTemplate} from './gr-diff-mode-selector_html.js';

/** @extends PolymerElement */
class GrDiffModeSelector extends GestureEventListeners(
    LegacyElementMixin(
        PolymerElement)) {
  static get template() { return htmlTemplate; }

  static get is() { return 'gr-diff-mode-selector'; }

  static get properties() {
    return {
      mode: {
        type: String,
        notify: true,
      },

      /**
       * If set to true, the user's preference will be updated every time a
       * button is tapped. Don't set to true if there is no user.
       */
      saveOnChange: {
        type: Boolean,
        value: false,
      },

      /** @type {?} */
      _VIEW_MODES: {
        type: Object,
        readOnly: true,
        value: {
          SIDE_BY_SIDE: 'SIDE_BY_SIDE',
          UNIFIED: 'UNIFIED_DIFF',
        },
      },
    };
  }

  /**
   * Set the mode. If save on change is enabled also update the preference.
   */
  setMode(newMode) {
    if (this.saveOnChange && this.mode && this.mode !== newMode) {
      this.$.restAPI.savePreferences({diff_view: newMode});
    }
    this.mode = newMode;
  }

  _computeSelectedClass(diffViewMode, buttonViewMode) {
    return buttonViewMode === diffViewMode ? 'selected' : '';
  }

  _handleSideBySideTap() {
    this.setMode(this._VIEW_MODES.SIDE_BY_SIDE);
  }

  _handleUnifiedTap() {
    this.setMode(this._VIEW_MODES.UNIFIED);
  }
}

customElements.define(GrDiffModeSelector.is, GrDiffModeSelector);
