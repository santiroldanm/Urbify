-- Asegurar que propietario_id permita NULL
ALTER TABLE apartamentos
MODIFY propietario_id BIGINT NULL;

-- Eliminar la FK si existe (reemplazá el nombre real de la FK si es distinto)
ALTER TABLE apartamentos
DROP FOREIGN KEY fk_apartamento_propietario;

-- Crear la nueva FK con ON DELETE SET NULL
ALTER TABLE apartamentos
ADD CONSTRAINT fk_apartamento_propietario
FOREIGN KEY (propietario_id)
REFERENCES propietario(id)
ON DELETE SET NULL;
