package com.bi.idp.be.controller;

import com.bi.idp.be.dto.SettingsDTO;
import com.bi.idp.be.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Controller for managing user settings
 */
@Controller
@RequestMapping("/settings")
public class SettingsController {
    private SettingsService settingsService;

    @Autowired
    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    /**
     * Get current user settings
     *
     *
     * @return current settings data
     */
    @GetMapping("/current")
    public ResponseEntity getCurrentUserSettings() {
        return ok(settingsService.getCurrentSettings());
    }

    /**
     * Update current user settings
     *
     * @param settingsDTO updated settings data
     * @return updated settings data
     */
    @PutMapping("/current")
    public ResponseEntity updateCurrentUserSettings(@Valid @RequestBody SettingsDTO settingsDTO) {
        SettingsDTO updatedSettings = settingsService.updateCurrentSettings(settingsDTO);
        return ok(updatedSettings);
    }
}
