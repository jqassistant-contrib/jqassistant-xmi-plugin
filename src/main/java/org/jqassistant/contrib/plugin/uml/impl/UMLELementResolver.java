package org.jqassistant.contrib.plugin.uml.impl;

import com.buschmais.jqassistant.core.store.api.Store;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jqassistant.contrib.plugin.uml.api.UMLElementDescriptor;
import org.jqassistant.contrib.plugin.uml.api.UMLModelDescriptor;

/**
 * A resolver for UML elements.
 */
@Getter
@RequiredArgsConstructor
class UMLELementResolver {

    /**
     * The {@link UMLModelDescriptor} declaring all resolved {@link UMLElementDescriptor}s.
     */
    private final UMLModelDescriptor umlModelDescriptor;

    private final String xmiNamespace;

    /**
     * The {@link Store}.
     */
    private final Store store;

    /**
     * The {@link Cache}.
     */
    private final Cache<String, UMLElementDescriptor> cache = Caffeine.newBuilder().softValues().build();

    /**
     * Create a {@link UMLElementDescriptor}.
     *
     * @param xmiId  The XMI id of the {@link UMLElementDescriptor}.
     * @param type   The requested type of the {@link UMLElementDescriptor}.
     * @param parent The parent of the {@link UMLElementDescriptor}.
     * @param <T>    The type of the {@link UMLElementDescriptor}.
     * @return The created {@link UMLElementDescriptor}.
     */
    <T extends UMLElementDescriptor> T create(String xmiId, Class<T> type, UMLElementDescriptor parent) {
        T umlElement = store.addDescriptorType(resolve(xmiId), type);
        umlElement.setParent(parent);
        return umlElement;
    }

    /**
     * Resolve a {@link UMLElementDescriptor}, even if it has not been created yet.
     *
     * @param xmiIdRef The XMI id referencing a {@link UMLElementDescriptor}.
     * @return The resolved {@link UMLElementDescriptor}.
     */
    UMLElementDescriptor resolve(String xmiIdRef) {
        return cache.get(xmiIdRef, key -> umlModelDescriptor.resolveElement(key));
    }

}