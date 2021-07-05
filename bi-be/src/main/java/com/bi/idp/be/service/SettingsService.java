package com.bi.idp.be.service;

import com.bi.idp.be.config.user.UserContextHolder;
import com.bi.idp.be.dto.SettingsDTO;
import com.bi.idp.be.exception.settings.SettingsNotFoundHttpException;
import com.bi.idp.be.model.Settings;
import com.bi.idp.be.model.user.User;
import com.bi.idp.be.repository.SettingsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {
    private ModelMapper modelMapper;
    private SettingsRepository settingsRepository;

    @Autowired
    public SettingsService(ModelMapper modelMapper,
                           SettingsRepository settingsRepository) {
        this.modelMapper = modelMapper;
        this.settingsRepository = settingsRepository;
    }

    public Settings getSettingsByUserId(Long id) {
        return settingsRepository.findById(id).orElseThrow(
                () -> new SettingsNotFoundHttpException("Setting with id: " + id + " not found", HttpStatus.NOT_FOUND)
        );
    }

    public SettingsDTO getCurrentSettings() {
        User currentUser = UserContextHolder.getUser();
        Settings settings = getSettingsByUserId(currentUser.getId());
        return modelMapper.map(settings, SettingsDTO.class);
    }

    public SettingsDTO updateCurrentSettings(SettingsDTO settingsDTO) {
        User user = UserContextHolder.getUser();
        Long id = user.getId();

        return updateSettingsByUserId(id, settingsDTO);
    }

    private SettingsDTO updateSettingsByUserId(Long id, SettingsDTO settingsDTO) {
        settingsRepository.findById(id).orElseThrow(() ->
                new SettingsNotFoundHttpException("Setting with id: " + id + " not found", HttpStatus.NOT_FOUND)
        );

        Settings updatedSettings = modelMapper.map(settingsDTO, Settings.class);
        updatedSettings.setId(id);
        settingsRepository.save(updatedSettings);
        return settingsDTO;
    }
}
