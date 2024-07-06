insert into power_transformers (power_transformers_id, high_side_voltage, idle_current, low_side_voltage,
                                short_circuit_losses, short_circuit_voltage, transformer_full_power,
                                transformer_idle_losses, transformer_load_coef, transformer_model)
values (3, 10, 0.5, 0.4, 10.5, 5.5, 250, 1400, 0.01, 'TMG-1000'),
       (6, 10, 0.5, 0.4, 10.5, 5.5, 250, 140, 0.1, 'TMG-100');